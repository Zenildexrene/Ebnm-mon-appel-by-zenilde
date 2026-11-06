package com.example.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.AppImageItem
import com.example.data.ReaderPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryModal(
    palette: ReaderPalette,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val images = remember { AppImageItem.getAllApkImages() }
    var selectedImageIndex by remember { mutableStateOf<Int?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = palette.bar,
        contentColor = palette.fg,
        modifier = Modifier.testTag("gallery_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(palette.accent.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Collections,
                            contentDescription = null,
                            tint = palette.accent,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Galerie des Images & Archives",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.accent
                        )
                        Text(
                            text = "${images.size} photos historiques dans l'ouvrage",
                            fontSize = 12.sp,
                            color = palette.fg.copy(alpha = 0.6f)
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_gallery_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fermer",
                        tint = palette.fg
                    )
                }
            }

            // Grid of Images
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 4.dp)
            ) {
                itemsIndexed(images) { index, item ->
                    val resId = remember(item.resName) {
                        context.resources.getIdentifier(item.resName, "drawable", context.packageName)
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = palette.bg),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, palette.accent.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .clickable { selectedImageIndex = index }
                            .testTag("gallery_item_$index")
                    ) {
                        Column {
                            if (resId != 0) {
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = item.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(130.dp)
                                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                )
                            }
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = item.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = palette.accent,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.caption,
                                    fontSize = 11.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = palette.fg.copy(alpha = 0.8f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    // Full-screen Image Detail Dialog when an item is selected
    selectedImageIndex?.let { index ->
        val currentItem = images[index]
        val resId = remember(currentItem.resName) {
            context.resources.getIdentifier(currentItem.resName, "drawable", context.packageName)
        }

        Dialog(
            onDismissRequest = { selectedImageIndex = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                color = palette.bg,
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Top Bar
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${index + 1} / ${images.size}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.accent
                        )

                        IconButton(
                            onClick = { selectedImageIndex = null },
                            modifier = Modifier.testTag("close_detail_dialog")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fermer",
                                tint = palette.accent
                            )
                        }
                    }

                    // Content Area (Scrollable)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 8.dp)
                    ) {
                        if (resId != 0) {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = palette.bar),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = currentItem.title,
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                )
                            }
                        }

                        Text(
                            text = currentItem.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.accent,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            color = palette.bar,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Légende : ${currentItem.caption}",
                                fontSize = 13.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.SemiBold,
                                color = palette.accent,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = palette.accent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Détails historiques",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = palette.fg
                            )
                        }

                        Text(
                            text = currentItem.details,
                            fontSize = 14.sp,
                            color = palette.fg,
                            lineHeight = 22.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Navigation Controls (Previous / Next Image)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                selectedImageIndex = if (index > 0) index - 1 else images.size - 1
                            },
                            shape = RoundedCornerShape(20.dp),
                            border = ButtonDefaults.outlinedToolboxBorder(palette.accent),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = palette.accent)
                        ) {
                            Icon(Icons.Default.NavigateBefore, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Précédente")
                        }

                        OutlinedButton(
                            onClick = {
                                selectedImageIndex = (index + 1) % images.size
                            },
                            shape = RoundedCornerShape(20.dp),
                            border = ButtonDefaults.outlinedToolboxBorder(palette.accent),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = palette.accent)
                        ) {
                            Text("Suivante")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.NavigateNext, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ButtonDefaults.outlinedToolboxBorder(color: Color) =
    androidx.compose.foundation.BorderStroke(1.dp, color)
