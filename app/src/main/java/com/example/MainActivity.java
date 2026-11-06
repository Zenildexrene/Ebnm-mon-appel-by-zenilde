package com.example;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.example.data.AppImageItem;
import com.example.data.Chapter;
import com.example.data.ChapterItem;
import com.example.data.ReaderPalette;
import com.example.data.ReaderRepository;
import com.example.data.SearchResult;
import com.example.data.StringsProvider;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ReaderRepository repository;
    private List<Chapter> chapters;
    private int currentChapterIndex = 0;
    private int currentThemeIndex = 0;
    private float fontSize = 19f;
    private boolean useSerif = true;
    private Set<String> bookmarks;
    private int scrollY = 0;
    private boolean isHomeView = true;

    private FrameLayout rootContainer;
    private ScrollView mainScrollView;
    private LinearLayout contentLayout;
    private ReaderPalette currentPalette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repository = ReaderRepository.get(getApplicationContext());
        chapters = repository.getChapters();
        currentThemeIndex = repository.getThemeIndex();
        fontSize = repository.getFontSize();
        useSerif = repository.getUseSerif();
        bookmarks = repository.getBookmarks();
        currentChapterIndex = repository.getLastChapter();
        if (currentChapterIndex < 0 || currentChapterIndex >= chapters.size()) {
            currentChapterIndex = 0;
        }
        scrollY = repository.getLastScrollY();

        rootContainer = new FrameLayout(this);
        setContentView(rootContainer);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!isHomeView) {
                    showHomeView();
                } else {
                    finish();
                }
            }
        });

        showHomeView();
    }

    private ReaderPalette getPalette() {
        List<ReaderPalette> palettes = ReaderPalette.getPalettes();
        if (currentThemeIndex < 0 || currentThemeIndex >= palettes.size()) {
            currentThemeIndex = 0;
        }
        return palettes.get(currentThemeIndex);
    }

    // ==========================================
    // HOME SCREEN (Multilingual & Material UI)
    // ==========================================
    private void showHomeView() {
        isHomeView = true;
        currentPalette = getPalette();
        String currentLang = repository.getLanguage();

        rootContainer.removeAllViews();
        rootContainer.setBackgroundColor(currentPalette.getBg());

        mainScrollView = new ScrollView(this);
        mainScrollView.setFillViewport(true);

        LinearLayout homeLayout = new LinearLayout(this);
        homeLayout.setOrientation(LinearLayout.VERTICAL);
        homeLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        int pad = dpToPx(24);
        homeLayout.setPadding(pad, dpToPx(32), pad, dpToPx(40));

        // 1. KCC Medallion Logo
        FrameLayout logoContainer = new FrameLayout(this);
        int logoSize = dpToPx(110);
        LinearLayout.LayoutParams logoParams = new LinearLayout.LayoutParams(logoSize, logoSize);
        logoParams.bottomMargin = dpToPx(16);
        logoContainer.setLayoutParams(logoParams);

        GradientDrawable logoBg = new GradientDrawable();
        logoBg.setShape(GradientDrawable.OVAL);
        logoBg.setColor(Color.WHITE);
        logoBg.setStroke(dpToPx(3), currentPalette.getAccent());
        logoContainer.setBackground(logoBg);
        logoContainer.setClipToOutline(true);

        ImageView logoImage = new ImageView(this);
        logoImage.setImageResource(R.drawable.kcc_logo);
        logoImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        logoImage.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

        logoContainer.addView(logoImage, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        homeLayout.addView(logoContainer);

        // 2. Main Title (Localized)
        TextView titleText = new TextView(this);
        titleText.setText(StringsProvider.getTitle(currentLang));
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
        titleText.setTypeface(Typeface.SERIF, Typeface.BOLD);
        titleText.setTextColor(currentPalette.getAccent());
        titleText.setGravity(Gravity.CENTER);
        titleText.setLetterSpacing(0.08f);
        homeLayout.addView(titleText);

        // 3. Author Info (Localized)
        TextView authorText = new TextView(this);
        authorText.setText(StringsProvider.getSubtitle(currentLang));
        authorText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        authorText.setTypeface(Typeface.DEFAULT_BOLD);
        authorText.setTextColor(currentPalette.getFg());
        authorText.setGravity(Gravity.CENTER);
        authorText.setLineSpacing(0, 1.25f);
        LinearLayout.LayoutParams authorParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        authorParams.topMargin = dpToPx(8);
        authorText.setLayoutParams(authorParams);
        homeLayout.addView(authorText);

        // Date
        TextView dateText = new TextView(this);
        dateText.setText("Juillet 2010");
        dateText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        dateText.setTextColor(adjustAlpha(currentPalette.getFg(), 0.6f));
        dateText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dateParams.topMargin = dpToPx(4);
        dateParams.bottomMargin = dpToPx(24);
        dateText.setLayoutParams(dateParams);
        homeLayout.addView(dateText);

        // 4. Primary Button (Commencer / Reprendre)
        boolean hasProgress = repository.hasProgress();
        LinearLayout startBtn = createStyledButtonWithIcon(
                StringsProvider.getBtnStart(currentLang, hasProgress),
                hasProgress ? R.drawable.ic_play : R.drawable.ic_book,
                currentPalette.getAccent(),
                currentPalette.getBg(),
                true
        );
        startBtn.setOnClickListener(v -> {
            if (!repository.hasProgress()) {
                currentChapterIndex = 0;
                scrollY = 0;
            }
            showReaderView();
        });
        homeLayout.addView(startBtn);

        // 5. Table of Chapters Button
        LinearLayout chaptersBtn = createStyledButtonWithIcon(
                StringsProvider.getBtnChapters(currentLang),
                R.drawable.ic_list,
                Color.TRANSPARENT,
                currentPalette.getAccent(),
                false
        );
        chaptersBtn.setOnClickListener(v -> showChapterListDialog());
        homeLayout.addView(chaptersBtn);

        // 6. Row Buttons (Rechercher & À propos)
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rowParams.bottomMargin = dpToPx(12);
        rowLayout.setLayoutParams(rowParams);

        LinearLayout searchBtn = createStyledButtonWithIcon(
                StringsProvider.getBtnSearch(currentLang), R.drawable.ic_search, Color.TRANSPARENT, currentPalette.getAccent(), false);
        LinearLayout.LayoutParams searchParams = new LinearLayout.LayoutParams(0, dpToPx(50), 1f);
        searchParams.rightMargin = dpToPx(6);
        searchBtn.setLayoutParams(searchParams);
        searchBtn.setOnClickListener(v -> showSearchDialog());
        rowLayout.addView(searchBtn);

        LinearLayout aboutBtn = createStyledButtonWithIcon(
                StringsProvider.getBtnAbout(currentLang), R.drawable.ic_info, Color.TRANSPARENT, currentPalette.getAccent(), false);
        LinearLayout.LayoutParams aboutParams = new LinearLayout.LayoutParams(0, dpToPx(50), 1f);
        aboutParams.leftMargin = dpToPx(6);
        aboutBtn.setLayoutParams(aboutParams);
        aboutBtn.setOnClickListener(v -> showAboutDialog());
        rowLayout.addView(aboutBtn);

        homeLayout.addView(rowLayout);

        // 7. Gallery Button
        LinearLayout galleryBtn = createStyledButtonWithIcon(
                StringsProvider.getBtnGallery(currentLang),
                R.drawable.ic_gallery,
                Color.TRANSPARENT,
                currentPalette.getAccent(),
                false
        );
        galleryBtn.setOnClickListener(v -> showGalleryDialog());
        homeLayout.addView(galleryBtn);

        // 8. Language Chips Selector
        TextView langLabel = new TextView(this);
        langLabel.setText("🌐 " + StringsProvider.getLangLabel(currentLang));
        langLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        langLabel.setTypeface(Typeface.DEFAULT_BOLD);
        langLabel.setTextColor(adjustAlpha(currentPalette.getFg(), 0.7f));
        LinearLayout.LayoutParams lLabelParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lLabelParams.topMargin = dpToPx(20);
        lLabelParams.bottomMargin = dpToPx(8);
        langLabel.setLayoutParams(lLabelParams);
        homeLayout.addView(langLabel);

        LinearLayout langChipsLayout = new LinearLayout(this);
        langChipsLayout.setOrientation(LinearLayout.HORIZONTAL);
        langChipsLayout.setGravity(Gravity.CENTER);

        String[][] languages = new String[][] {
            {"fr", "Français"},
            {"ln", "Lingála"},
            {"en", "English"},
            {"pt", "Português"},
            {"es", "Español"}
        };

        for (String[] langArr : languages) {
            final String code = langArr[0];
            String name = langArr[1];
            boolean isSelected = code.equals(currentLang);

            TextView chip = new TextView(this);
            chip.setText(name);
            chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            chip.setTypeface(Typeface.DEFAULT_BOLD);
            chip.setPadding(dpToPx(12), dpToPx(6), dpToPx(12), dpToPx(6));

            GradientDrawable chipBg = new GradientDrawable();
            chipBg.setCornerRadius(dpToPx(16));
            if (isSelected) {
                chipBg.setColor(currentPalette.getAccent());
                chip.setTextColor(currentPalette.getBg());
            } else {
                chipBg.setColor(currentPalette.getBg());
                chipBg.setStroke(dpToPx(1), adjustAlpha(currentPalette.getAccent(), 0.4f));
                chip.setTextColor(currentPalette.getAccent());
            }
            chip.setBackground(chipBg);

            LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cParams.leftMargin = dpToPx(3);
            cParams.rightMargin = dpToPx(3);
            chip.setLayoutParams(cParams);

            chip.setOnClickListener(v -> {
                repository.setLanguage(code);
                chapters = repository.getChapters();
                showHomeView();
            });
            langChipsLayout.addView(chip);
        }
        homeLayout.addView(langChipsLayout);

        // 9. Theme Chips Selector
        TextView themeLabel = new TextView(this);
        themeLabel.setText("🎨 " + StringsProvider.getThemeLabel(currentLang));
        themeLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        themeLabel.setTypeface(Typeface.DEFAULT_BOLD);
        themeLabel.setTextColor(adjustAlpha(currentPalette.getFg(), 0.7f));
        LinearLayout.LayoutParams tLabelParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tLabelParams.topMargin = dpToPx(16);
        tLabelParams.bottomMargin = dpToPx(8);
        themeLabel.setLayoutParams(tLabelParams);
        homeLayout.addView(themeLabel);

        LinearLayout themeChipsLayout = new LinearLayout(this);
        themeChipsLayout.setOrientation(LinearLayout.HORIZONTAL);
        themeChipsLayout.setGravity(Gravity.CENTER);

        List<ReaderPalette> palettes = ReaderPalette.getPalettes();
        for (int i = 0; i < palettes.size(); i++) {
            final int themeIdx = i;
            ReaderPalette p = palettes.get(i);
            boolean isSelected = (i == currentThemeIndex);

            TextView chip = new TextView(this);
            chip.setText(p.getName());
            chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            chip.setTypeface(Typeface.DEFAULT_BOLD);
            chip.setPadding(dpToPx(14), dpToPx(6), dpToPx(14), dpToPx(6));

            GradientDrawable chipBg = new GradientDrawable();
            chipBg.setCornerRadius(dpToPx(16));
            if (isSelected) {
                chipBg.setColor(p.getAccent());
                chip.setTextColor(p.getBg());
            } else {
                chipBg.setColor(p.getBg());
                chipBg.setStroke(dpToPx(1), adjustAlpha(p.getAccent(), 0.4f));
                chip.setTextColor(p.getAccent());
            }
            chip.setBackground(chipBg);

            LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cParams.leftMargin = dpToPx(3);
            cParams.rightMargin = dpToPx(3);
            chip.setLayoutParams(cParams);

            chip.setOnClickListener(v -> {
                currentThemeIndex = themeIdx;
                repository.setThemeIndex(themeIdx);
                showHomeView();
            });
            themeChipsLayout.addView(chip);
        }
        homeLayout.addView(themeChipsLayout);

        // 10. Footer
        LinearLayout footerLayout = new LinearLayout(this);
        footerLayout.setOrientation(LinearLayout.HORIZONTAL);
        footerLayout.setGravity(Gravity.CENTER);

        ImageView devIcon = new ImageView(this);
        devIcon.setImageResource(R.drawable.ic_developer);
        devIcon.setColorFilter(currentPalette.getAccent());
        LinearLayout.LayoutParams dIconParams = new LinearLayout.LayoutParams(dpToPx(16), dpToPx(16));
        dIconParams.rightMargin = dpToPx(6);
        devIcon.setLayoutParams(dIconParams);
        footerLayout.addView(devIcon);

        TextView footerText = new TextView(this);
        footerText.setText("Application développée par Zenilde Lutu");
        footerText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        footerText.setTextColor(adjustAlpha(currentPalette.getFg(), 0.5f));
        footerLayout.addView(footerText);

        LinearLayout.LayoutParams fParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fParams.topMargin = dpToPx(28);
        footerLayout.setLayoutParams(fParams);
        homeLayout.addView(footerLayout);

        mainScrollView.addView(homeLayout);
        rootContainer.addView(mainScrollView);
    }

    // ==========================================
    // READER SCREEN
    // ==========================================
    private void showReaderView() {
        isHomeView = false;
        currentPalette = getPalette();
        String currentLang = repository.getLanguage();

        rootContainer.removeAllViews();

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(currentPalette.getBg());

        // TOP BAR
        LinearLayout topBar = new LinearLayout(this);
        topBar.setOrientation(LinearLayout.HORIZONTAL);
        topBar.setGravity(Gravity.CENTER_VERTICAL);
        topBar.setBackgroundColor(currentPalette.getBar());
        topBar.setPadding(dpToPx(6), dpToPx(6), dpToPx(6), dpToPx(6));
        topBar.setElevation(dpToPx(4));

        ImageView backBtn = createTopBarIconButton(R.drawable.ic_arrow_back, currentPalette.getAccent());
        backBtn.setOnClickListener(v -> {
            if (mainScrollView != null) {
                repository.setLastScrollY(mainScrollView.getScrollY());
            }
            repository.setLastChapter(currentChapterIndex);
            showHomeView();
        });
        topBar.addView(backBtn);

        Chapter currentChapter = chapters.get(currentChapterIndex);
        TextView headerTitle = new TextView(this);
        headerTitle.setText(currentChapter.getTitle());
        headerTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        headerTitle.setTypeface(Typeface.DEFAULT_BOLD);
        headerTitle.setTextColor(currentPalette.getAccent());
        headerTitle.setSingleLine(true);

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        titleParams.leftMargin = dpToPx(6);
        titleParams.rightMargin = dpToPx(4);
        headerTitle.setLayoutParams(titleParams);
        topBar.addView(headerTitle);

        TextView decFontBtn = createTextIconButton("A−", currentPalette.getAccent());
        decFontBtn.setOnClickListener(v -> {
            fontSize = Math.max(13f, fontSize - 1f);
            repository.setFontSize(fontSize);
            showReaderView();
        });
        topBar.addView(decFontBtn);

        TextView incFontBtn = createTextIconButton("A+", currentPalette.getAccent());
        incFontBtn.setOnClickListener(v -> {
            fontSize = Math.min(32f, fontSize + 1f);
            repository.setFontSize(fontSize);
            showReaderView();
        });
        topBar.addView(incFontBtn);

        ImageView themeBtn = createTopBarIconButton(R.drawable.ic_contrast, currentPalette.getAccent());
        themeBtn.setOnClickListener(v -> {
            currentThemeIndex = (currentThemeIndex + 1) % ReaderPalette.getPalettes().size();
            repository.setThemeIndex(currentThemeIndex);
            showReaderView();
        });
        topBar.addView(themeBtn);

        ImageView searchBtn = createTopBarIconButton(R.drawable.ic_search, currentPalette.getAccent());
        searchBtn.setOnClickListener(v -> showSearchDialog());
        topBar.addView(searchBtn);

        boolean isBookmarked = bookmarks.contains("ch_" + currentChapterIndex);
        ImageView bmBtn = createTopBarIconButton(
                isBookmarked ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_border,
                currentPalette.getAccent()
        );
        bmBtn.setOnClickListener(v -> {
            repository.toggleBookmark(currentChapterIndex);
            bookmarks = repository.getBookmarks();
            showReaderView();
        });
        topBar.addView(bmBtn);

        mainLayout.addView(topBar);

        View progressLine = new View(this);
        progressLine.setBackgroundColor(currentPalette.getAccent());
        mainLayout.addView(progressLine, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(3)));

        mainScrollView = new ScrollView(this);
        LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
        mainScrollView.setLayoutParams(scrollParams);

        contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setPadding(dpToPx(22), dpToPx(20), dpToPx(22), dpToPx(40));

        TextView chapterHeader = new TextView(this);
        chapterHeader.setText(currentChapter.getTitle());
        chapterHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize + 6f);
        chapterHeader.setTypeface(useSerif ? Typeface.SERIF : Typeface.DEFAULT, Typeface.BOLD);
        chapterHeader.setTextColor(currentPalette.getAccent());
        chapterHeader.setPadding(0, 0, 0, dpToPx(20));
        contentLayout.addView(chapterHeader);

        List<ChapterItem> items = ChapterItem.parseChapterItems(currentChapter.getBody());
        for (ChapterItem item : items) {
            if (item.getType() == ChapterItem.Type.TEXT) {
                TextView pText = new TextView(this);
                pText.setText(item.getContent());
                pText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                pText.setTypeface(useSerif ? Typeface.SERIF : Typeface.DEFAULT);
                pText.setTextColor(currentPalette.getFg());
                pText.setLineSpacing(0, 1.5f);
                pText.setPadding(0, 0, 0, dpToPx(16));
                contentLayout.addView(pText);
            } else if (item.getType() == ChapterItem.Type.IMAGE) {
                int resId = getResources().getIdentifier(item.getResName(), "drawable", getPackageName());
                if (resId != 0) {
                    LinearLayout imgCard = new LinearLayout(this);
                    imgCard.setOrientation(LinearLayout.VERTICAL);
                    imgCard.setGravity(Gravity.CENTER_HORIZONTAL);

                    GradientDrawable cardBg = new GradientDrawable();
                    cardBg.setColor(currentPalette.getBar());
                    cardBg.setCornerRadius(dpToPx(16));
                    imgCard.setBackground(cardBg);
                    imgCard.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

                    LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    cardParams.topMargin = dpToPx(12);
                    cardParams.bottomMargin = dpToPx(16);
                    imgCard.setLayoutParams(cardParams);

                    ImageView imgView = new ImageView(this);
                    imgView.setImageResource(resId);
                    imgView.setAdjustViewBounds(true);
                    imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imgCard.addView(imgView);

                    if (item.getCaption() != null && !item.getCaption().isEmpty()) {
                        TextView captionText = new TextView(this);
                        captionText.setText(item.getCaption());
                        captionText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Math.max(12f, fontSize - 3f));
                        captionText.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
                        captionText.setTextColor(currentPalette.getAccent());
                        captionText.setGravity(Gravity.CENTER);
                        captionText.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), 0);
                        imgCard.addView(captionText);
                    }

                    contentLayout.addView(imgCard);
                }
            }
        }

        mainScrollView.addView(contentLayout);
        mainLayout.addView(mainScrollView);

        if (scrollY > 0) {
            mainScrollView.post(() -> mainScrollView.scrollTo(0, scrollY));
        }

        // BOTTOM NAV BAR
        LinearLayout bottomBar = new LinearLayout(this);
        bottomBar.setOrientation(LinearLayout.HORIZONTAL);
        bottomBar.setGravity(Gravity.CENTER_VERTICAL);
        bottomBar.setBackgroundColor(currentPalette.getBar());
        bottomBar.setPadding(dpToPx(12), dpToPx(8), dpToPx(12), dpToPx(8));
        bottomBar.setElevation(dpToPx(8));

        LinearLayout prevBtn = createNavButton(StringsProvider.getBtnPrev(currentLang), R.drawable.ic_chevron_left, currentChapterIndex > 0, currentPalette);
        prevBtn.setOnClickListener(v -> {
            if (currentChapterIndex > 0) {
                currentChapterIndex--;
                scrollY = 0;
                repository.setLastChapter(currentChapterIndex);
                repository.setLastScrollY(0);
                showReaderView();
            }
        });
        bottomBar.addView(prevBtn, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        LinearLayout chapterPickerBtn = createStyledButtonWithIcon(
                (currentChapterIndex + 1) + "/" + chapters.size() + " " + StringsProvider.getChapterSuffix(currentLang),
                R.drawable.ic_list,
                Color.TRANSPARENT,
                currentPalette.getAccent(),
                false
        );
        LinearLayout.LayoutParams pickerParams = new LinearLayout.LayoutParams(0, dpToPx(42), 1.3f);
        pickerParams.leftMargin = dpToPx(6);
        pickerParams.rightMargin = dpToPx(6);
        chapterPickerBtn.setLayoutParams(pickerParams);
        chapterPickerBtn.setOnClickListener(v -> showChapterListDialog());
        bottomBar.addView(chapterPickerBtn);

        LinearLayout nextBtn = createNavButton(StringsProvider.getBtnNext(currentLang), R.drawable.ic_chevron_right, currentChapterIndex < chapters.size() - 1, currentPalette);
        nextBtn.setOnClickListener(v -> {
            if (currentChapterIndex < chapters.size() - 1) {
                currentChapterIndex++;
                scrollY = 0;
                repository.setLastChapter(currentChapterIndex);
                repository.setLastScrollY(0);
                showReaderView();
            }
        });
        bottomBar.addView(nextBtn, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        mainLayout.addView(bottomBar);

        rootContainer.addView(mainLayout);
    }

    // ==========================================
    // DIALOGS
    // ==========================================

    private void showChapterListDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setBackgroundColor(currentPalette.getBar());
        dialogLayout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(24));

        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(Gravity.CENTER_VERTICAL);
        headerRow.setPadding(0, 0, 0, dpToPx(12));

        ImageView listIcon = new ImageView(this);
        listIcon.setImageResource(R.drawable.ic_list);
        listIcon.setColorFilter(currentPalette.getAccent());
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(dpToPx(24), dpToPx(24));
        lParams.rightMargin = dpToPx(8);
        listIcon.setLayoutParams(lParams);
        headerRow.addView(listIcon);

        TextView dTitle = new TextView(this);
        dTitle.setText(StringsProvider.getBtnChapters(repository.getLanguage()) + " (" + chapters.size() + ")");
        dTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        dTitle.setTypeface(Typeface.DEFAULT_BOLD);
        dTitle.setTextColor(currentPalette.getAccent());
        headerRow.addView(dTitle, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        ImageView closeBtn = new ImageView(this);
        closeBtn.setImageResource(R.drawable.ic_close);
        closeBtn.setColorFilter(currentPalette.getFg());
        closeBtn.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
        closeBtn.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(32), dpToPx(32)));
        closeBtn.setOnClickListener(v -> dialog.dismiss());
        headerRow.addView(closeBtn);

        dialogLayout.addView(headerRow);

        EditText searchEdit = new EditText(this);
        searchEdit.setHint("...");
        searchEdit.setHintTextColor(adjustAlpha(currentPalette.getFg(), 0.5f));
        searchEdit.setTextColor(currentPalette.getFg());
        searchEdit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        GradientDrawable editBg = new GradientDrawable();
        editBg.setColor(currentPalette.getBg());
        editBg.setCornerRadius(dpToPx(12));
        editBg.setStroke(dpToPx(1), adjustAlpha(currentPalette.getFg(), 0.3f));
        searchEdit.setBackground(editBg);
        searchEdit.setPadding(dpToPx(14), dpToPx(10), dpToPx(14), dpToPx(10));
        dialogLayout.addView(searchEdit);

        ScrollView scrollList = new ScrollView(this);
        LinearLayout itemsContainer = new LinearLayout(this);
        itemsContainer.setOrientation(LinearLayout.VERTICAL);
        itemsContainer.setPadding(0, dpToPx(12), 0, 0);

        Runnable updateList = () -> {
            itemsContainer.removeAllViews();
            String query = searchEdit.getText().toString().toLowerCase().trim();

            for (int i = 0; i < chapters.size(); i++) {
                final int chIdx = i;
                Chapter ch = chapters.get(i);

                if (!query.isEmpty() && !ch.getTitle().toLowerCase().contains(query) && !ch.getBody().toLowerCase().contains(query)) {
                    continue;
                }

                boolean isSelected = (i == currentChapterIndex);
                boolean isBookmarked = bookmarks.contains("ch_" + i);

                LinearLayout itemRow = new LinearLayout(this);
                itemRow.setOrientation(LinearLayout.HORIZONTAL);
                itemRow.setGravity(Gravity.CENTER_VERTICAL);
                itemRow.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

                GradientDrawable itemBg = new GradientDrawable();
                itemBg.setCornerRadius(dpToPx(12));
                itemBg.setColor(isSelected ? adjustAlpha(currentPalette.getAccent(), 0.18f) : currentPalette.getBg());
                itemRow.setBackground(itemBg);

                LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rowParams.bottomMargin = dpToPx(8);
                itemRow.setLayoutParams(rowParams);

                TextView badge = new TextView(this);
                badge.setText(String.valueOf(i + 1));
                badge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                badge.setTypeface(Typeface.DEFAULT_BOLD);
                badge.setTextColor(isSelected ? currentPalette.getBg() : currentPalette.getAccent());
                badge.setGravity(Gravity.CENTER);

                GradientDrawable badgeBg = new GradientDrawable();
                badgeBg.setCornerRadius(dpToPx(8));
                badgeBg.setColor(isSelected ? currentPalette.getAccent() : currentPalette.getBar());
                badge.setBackground(badgeBg);

                LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(dpToPx(34), dpToPx(34));
                badgeParams.rightMargin = dpToPx(12);
                badge.setLayoutParams(badgeParams);
                itemRow.addView(badge);

                LinearLayout infoCol = new LinearLayout(this);
                infoCol.setOrientation(LinearLayout.VERTICAL);

                TextView titleTv = new TextView(this);
                titleTv.setText(ch.getTitle());
                titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                titleTv.setTypeface(Typeface.DEFAULT_BOLD);
                titleTv.setTextColor(isSelected ? currentPalette.getAccent() : currentPalette.getFg());
                infoCol.addView(titleTv);

                String snippet = ch.getBody().length() > 50 ? ch.getBody().substring(0, 50) + "..." : ch.getBody();
                TextView snippetTv = new TextView(this);
                snippetTv.setText(snippet.replace("\n", " "));
                snippetTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                snippetTv.setTextColor(adjustAlpha(currentPalette.getFg(), 0.6f));
                infoCol.addView(snippetTv);

                itemRow.addView(infoCol, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                ImageView bmIv = new ImageView(this);
                bmIv.setImageResource(isBookmarked ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_border);
                bmIv.setColorFilter(isBookmarked ? currentPalette.getAccent() : adjustAlpha(currentPalette.getFg(), 0.4f));
                bmIv.setPadding(dpToPx(6), dpToPx(6), dpToPx(6), dpToPx(6));
                LinearLayout.LayoutParams bmParams = new LinearLayout.LayoutParams(dpToPx(32), dpToPx(32));
                bmIv.setLayoutParams(bmParams);
                bmIv.setOnClickListener(v -> {
                    repository.toggleBookmark(chIdx);
                    bookmarks = repository.getBookmarks();
                    dialog.dismiss();
                    showChapterListDialog();
                });
                itemRow.addView(bmIv);

                itemRow.setOnClickListener(v -> {
                    currentChapterIndex = chIdx;
                    scrollY = 0;
                    repository.setLastChapter(chIdx);
                    repository.setLastScrollY(0);
                    dialog.dismiss();
                    showReaderView();
                });

                itemsContainer.addView(itemRow);
            }
        };

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { updateList.run(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        updateList.run();
        scrollList.addView(itemsContainer);
        dialogLayout.addView(scrollList, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(400)));

        dialog.setContentView(dialogLayout);
        dialog.show();
    }

    private void showSearchDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setBackgroundColor(currentPalette.getBar());
        dialogLayout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(24));

        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(Gravity.CENTER_VERTICAL);
        headerRow.setPadding(0, 0, 0, dpToPx(12));

        ImageView searchIcon = new ImageView(this);
        searchIcon.setImageResource(R.drawable.ic_search);
        searchIcon.setColorFilter(currentPalette.getAccent());
        LinearLayout.LayoutParams sParams = new LinearLayout.LayoutParams(dpToPx(24), dpToPx(24));
        sParams.rightMargin = dpToPx(8);
        searchIcon.setLayoutParams(sParams);
        headerRow.addView(searchIcon);

        TextView dTitle = new TextView(this);
        dTitle.setText(StringsProvider.getBtnSearch(repository.getLanguage()));
        dTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        dTitle.setTypeface(Typeface.DEFAULT_BOLD);
        dTitle.setTextColor(currentPalette.getAccent());
        headerRow.addView(dTitle, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        ImageView closeBtn = new ImageView(this);
        closeBtn.setImageResource(R.drawable.ic_close);
        closeBtn.setColorFilter(currentPalette.getFg());
        closeBtn.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
        closeBtn.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(32), dpToPx(32)));
        closeBtn.setOnClickListener(v -> dialog.dismiss());
        headerRow.addView(closeBtn);

        dialogLayout.addView(headerRow);

        EditText searchEdit = new EditText(this);
        searchEdit.setHint("...");
        searchEdit.setHintTextColor(adjustAlpha(currentPalette.getFg(), 0.5f));
        searchEdit.setTextColor(currentPalette.getFg());
        searchEdit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        GradientDrawable editBg = new GradientDrawable();
        editBg.setColor(currentPalette.getBg());
        editBg.setCornerRadius(dpToPx(12));
        editBg.setStroke(dpToPx(1), adjustAlpha(currentPalette.getFg(), 0.3f));
        searchEdit.setBackground(editBg);
        searchEdit.setPadding(dpToPx(14), dpToPx(10), dpToPx(14), dpToPx(10));
        dialogLayout.addView(searchEdit);

        TextView resultCountTv = new TextView(this);
        resultCountTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        resultCountTv.setTextColor(currentPalette.getAccent());
        resultCountTv.setPadding(0, dpToPx(8), 0, dpToPx(8));
        dialogLayout.addView(resultCountTv);

        ScrollView scrollResults = new ScrollView(this);
        LinearLayout resultsContainer = new LinearLayout(this);
        resultsContainer.setOrientation(LinearLayout.VERTICAL);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                resultsContainer.removeAllViews();
                String q = s.toString().trim();
                if (q.isEmpty()) {
                    resultCountTv.setText("");
                    return;
                }
                List<SearchResult> results = repository.search(q);
                resultCountTv.setText(results.size() + " resultat(s)");

                for (SearchResult res : results) {
                    LinearLayout resCard = new LinearLayout(MainActivity.this);
                    resCard.setOrientation(LinearLayout.VERTICAL);
                    resCard.setPadding(dpToPx(14), dpToPx(12), dpToPx(14), dpToPx(12));

                    GradientDrawable bg = new GradientDrawable();
                    bg.setColor(currentPalette.getBg());
                    bg.setCornerRadius(dpToPx(12));
                    resCard.setBackground(bg);

                    LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    cardParams.bottomMargin = dpToPx(8);
                    resCard.setLayoutParams(cardParams);

                    TextView chTitle = new TextView(MainActivity.this);
                    chTitle.setText(res.getChapterTitle());
                    chTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    chTitle.setTypeface(Typeface.DEFAULT_BOLD);
                    chTitle.setTextColor(currentPalette.getAccent());
                    resCard.addView(chTitle);

                    TextView snip = new TextView(MainActivity.this);
                    snip.setText(res.getSnippet());
                    snip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                    snip.setTextColor(currentPalette.getFg());
                    resCard.addView(snip);

                    resCard.setOnClickListener(v -> {
                        currentChapterIndex = res.getChapterIndex();
                        scrollY = 0;
                        repository.setLastChapter(currentChapterIndex);
                        repository.setLastScrollY(0);
                        dialog.dismiss();
                        showReaderView();
                    });

                    resultsContainer.addView(resCard);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        scrollResults.addView(resultsContainer);
        dialogLayout.addView(scrollResults, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(380)));

        dialog.setContentView(dialogLayout);
        dialog.show();
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setBackgroundColor(currentPalette.getBar());
        layout.setPadding(dpToPx(20), dpToPx(24), dpToPx(20), dpToPx(20));

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.kcc_logo);
        int lSize = dpToPx(80);
        layout.addView(logo, new LinearLayout.LayoutParams(lSize, lSize));

        String currentLang = repository.getLanguage();

        TextView title = new TextView(this);
        title.setText(StringsProvider.getTitle(currentLang));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);
        title.setTextColor(currentPalette.getAccent());
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, dpToPx(12), 0, dpToPx(4));
        layout.addView(title);

        TextView author = new TextView(this);
        author.setText(StringsProvider.getSubtitle(currentLang));
        author.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        author.setTypeface(Typeface.DEFAULT_BOLD);
        author.setTextColor(currentPalette.getFg());
        author.setGravity(Gravity.CENTER);
        author.setPadding(0, 0, 0, dpToPx(16));
        layout.addView(author);

        TextView body = new TextView(this);
        body.setText("Église Bon Nouveau Message (KCC)\nJuillet 2010\n\n© Texte : Dizolele-Mpungu Wafiduswa Isaac.\n\nApplication 100% Java développée par Zenilde Lutu\nVersion 1.0");
        body.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        body.setTextColor(adjustAlpha(currentPalette.getFg(), 0.8f));
        body.setGravity(Gravity.CENTER);
        layout.addView(body);

        builder.setView(layout);
        builder.setPositiveButton("OK", (d, w) -> d.dismiss());
        AlertDialog ad = builder.create();
        ad.show();
    }

    private void showGalleryDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setBackgroundColor(currentPalette.getBar());
        dialogLayout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(24));

        String currentLang = repository.getLanguage();
        List<AppImageItem> images = AppImageItem.getAllApkImages(currentLang);

        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(Gravity.CENTER_VERTICAL);
        headerRow.setPadding(0, 0, 0, dpToPx(12));

        ImageView galIcon = new ImageView(this);
        galIcon.setImageResource(R.drawable.ic_gallery);
        galIcon.setColorFilter(currentPalette.getAccent());
        LinearLayout.LayoutParams gParams = new LinearLayout.LayoutParams(dpToPx(24), dpToPx(24));
        gParams.rightMargin = dpToPx(8);
        galIcon.setLayoutParams(gParams);
        headerRow.addView(galIcon);

        TextView dTitle = new TextView(this);
        dTitle.setText(StringsProvider.getBtnGallery(currentLang) + " (" + images.size() + ")");
        dTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        dTitle.setTypeface(Typeface.DEFAULT_BOLD);
        dTitle.setTextColor(currentPalette.getAccent());
        headerRow.addView(dTitle, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        ImageView closeBtn = new ImageView(this);
        closeBtn.setImageResource(R.drawable.ic_close);
        closeBtn.setColorFilter(currentPalette.getFg());
        closeBtn.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
        closeBtn.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(32), dpToPx(32)));
        closeBtn.setOnClickListener(v -> dialog.dismiss());
        headerRow.addView(closeBtn);

        dialogLayout.addView(headerRow);

        ScrollView scrollGallery = new ScrollView(this);
        LinearLayout galleryContainer = new LinearLayout(this);
        galleryContainer.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < images.size(); i++) {
            final int imgIdx = i;
            AppImageItem item = images.get(i);
            int resId = getResources().getIdentifier(item.getResName(), "drawable", getPackageName());

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.HORIZONTAL);
            card.setGravity(Gravity.CENTER_VERTICAL);
            card.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));

            GradientDrawable cardBg = new GradientDrawable();
            cardBg.setColor(currentPalette.getBg());
            cardBg.setCornerRadius(dpToPx(12));
            cardBg.setStroke(dpToPx(1), adjustAlpha(currentPalette.getAccent(), 0.3f));
            card.setBackground(cardBg);

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cardParams.bottomMargin = dpToPx(10);
            card.setLayoutParams(cardParams);

            if (resId != 0) {
                ImageView thumb = new ImageView(this);
                thumb.setImageResource(resId);
                thumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
                LinearLayout.LayoutParams tParams = new LinearLayout.LayoutParams(dpToPx(75), dpToPx(75));
                tParams.rightMargin = dpToPx(12);
                thumb.setLayoutParams(tParams);
                card.addView(thumb);
            }

            LinearLayout infoCol = new LinearLayout(this);
            infoCol.setOrientation(LinearLayout.VERTICAL);

            TextView titleTv = new TextView(this);
            titleTv.setText(item.getTitle());
            titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            titleTv.setTypeface(Typeface.DEFAULT_BOLD);
            titleTv.setTextColor(currentPalette.getAccent());
            infoCol.addView(titleTv);

            TextView captionTv = new TextView(this);
            captionTv.setText(item.getCaption());
            captionTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            captionTv.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
            captionTv.setTextColor(currentPalette.getFg());
            infoCol.addView(captionTv);

            card.addView(infoCol, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            card.setOnClickListener(v -> {
                dialog.dismiss();
                showImageDetailDialog(imgIdx);
            });

            galleryContainer.addView(card);
        }

        scrollGallery.addView(galleryContainer);
        dialogLayout.addView(scrollGallery, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(420)));

        dialog.setContentView(dialogLayout);
        dialog.show();
    }

    private void showImageDetailDialog(int index) {
        String currentLang = repository.getLanguage();
        List<AppImageItem> images = AppImageItem.getAllApkImages(currentLang);
        if (index < 0 || index >= images.size()) return;

        AppImageItem item = images.get(index);
        int resId = getResources().getIdentifier(item.getResName(), "drawable", getPackageName());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ScrollView scroll = new ScrollView(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(currentPalette.getBg());
        layout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        if (resId != 0) {
            ImageView imgView = new ImageView(this);
            imgView.setImageResource(resId);
            imgView.setAdjustViewBounds(true);
            imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            layout.addView(imgView, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        TextView titleTv = new TextView(this);
        titleTv.setText(item.getTitle());
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        titleTv.setTypeface(Typeface.DEFAULT_BOLD);
        titleTv.setTextColor(currentPalette.getAccent());
        titleTv.setGravity(Gravity.CENTER);
        titleTv.setPadding(0, dpToPx(12), 0, dpToPx(4));
        layout.addView(titleTv);

        TextView captionTv = new TextView(this);
        captionTv.setText(item.getCaption());
        captionTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        captionTv.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        captionTv.setTextColor(currentPalette.getAccent());
        captionTv.setGravity(Gravity.CENTER);
        captionTv.setPadding(0, 0, 0, dpToPx(12));
        layout.addView(captionTv);

        TextView detailsTv = new TextView(this);
        detailsTv.setText(item.getDetails());
        detailsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        detailsTv.setTextColor(currentPalette.getFg());
        detailsTv.setLineSpacing(0, 1.3f);
        layout.addView(detailsTv);

        scroll.addView(layout);
        builder.setView(scroll);

        final int currentIndex = index;
        builder.setPositiveButton(StringsProvider.getBtnNext(currentLang) + " ▶", (d, w) -> {
            d.dismiss();
            showImageDetailDialog((currentIndex + 1) % images.size());
        });

        builder.setNegativeButton("◀ " + StringsProvider.getBtnPrev(currentLang), (d, w) -> {
            d.dismiss();
            showImageDetailDialog(currentIndex > 0 ? currentIndex - 1 : images.size() - 1);
        });

        builder.setNeutralButton("OK", (d, w) -> d.dismiss());

        AlertDialog ad = builder.create();
        ad.show();
    }

    // ==========================================
    // HELPER METHODS
    // ==========================================
    private LinearLayout createStyledButtonWithIcon(String text, int iconResId, int bgColor, int textColor, boolean filled) {
        LinearLayout btnLayout = new LinearLayout(this);
        btnLayout.setOrientation(LinearLayout.HORIZONTAL);
        btnLayout.setGravity(Gravity.CENTER);
        btnLayout.setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12));

        GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(dpToPx(28));
        if (filled) {
            bg.setColor(bgColor);
        } else {
            bg.setColor(Color.TRANSPARENT);
            bg.setStroke(dpToPx(1.5f), textColor);
        }
        btnLayout.setBackground(bg);

        if (iconResId != 0) {
            ImageView iconView = new ImageView(this);
            iconView.setImageResource(iconResId);
            iconView.setColorFilter(textColor);
            LinearLayout.LayoutParams iParams = new LinearLayout.LayoutParams(dpToPx(20), dpToPx(20));
            iParams.rightMargin = dpToPx(8);
            iconView.setLayoutParams(iParams);
            btnLayout.addView(iconView);
        }

        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextColor(textColor);
        btnLayout.addView(tv);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(52));
        params.bottomMargin = dpToPx(12);
        btnLayout.setLayoutParams(params);

        btnLayout.setFocusable(true);
        btnLayout.setClickable(true);
        return btnLayout;
    }

    private LinearLayout createNavButton(String text, int iconResId, boolean enabled, ReaderPalette palette) {
        LinearLayout btn = new LinearLayout(this);
        btn.setOrientation(LinearLayout.HORIZONTAL);
        btn.setGravity(Gravity.CENTER);
        btn.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));

        int color = enabled ? palette.getAccent() : adjustAlpha(palette.getAccent(), 0.3f);

        ImageView iv = new ImageView(this);
        iv.setImageResource(iconResId);
        iv.setColorFilter(color);
        LinearLayout.LayoutParams iParams = new LinearLayout.LayoutParams(dpToPx(18), dpToPx(18));
        iv.setLayoutParams(iParams);

        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextColor(color);

        if (iconResId == R.drawable.ic_chevron_left) {
            btn.addView(iv);
            btn.addView(tv);
        } else {
            btn.addView(tv);
            btn.addView(iv);
        }

        btn.setClickable(enabled);
        btn.setFocusable(enabled);
        return btn;
    }

    private ImageView createTopBarIconButton(int iconResId, int tintColor) {
        ImageView iv = new ImageView(this);
        iv.setImageResource(iconResId);
        iv.setColorFilter(tintColor);
        iv.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(42), dpToPx(42));
        iv.setLayoutParams(params);
        iv.setClickable(true);
        iv.setFocusable(true);
        return iv;
    }

    private TextView createTextIconButton(String text, int tintColor) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextColor(tintColor);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(dpToPx(10), dpToPx(6), dpToPx(10), dpToPx(6));
        tv.setClickable(true);
        tv.setFocusable(true);
        return tv;
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private int dpToPx(float dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }
}
