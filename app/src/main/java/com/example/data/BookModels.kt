package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

data class ReaderPalette(
    val id: Int,
    val name: String,
    val bg: Color,
    val fg: Color,
    val bar: Color,
    val accent: Color
)

val readerPalettes = listOf(
    ReaderPalette(0, "Nuit", NightBg, NightFg, NightBar, NightAccent),
    ReaderPalette(1, "Jour", DayBg, DayFg, DayBar, DayAccent),
    ReaderPalette(2, "Sépia", SepiaBg, SepiaFg, SepiaBar, SepiaAccent),
    ReaderPalette(3, "Sombre", DarkBg, DarkFg, DarkBar, DarkAccent)
)
