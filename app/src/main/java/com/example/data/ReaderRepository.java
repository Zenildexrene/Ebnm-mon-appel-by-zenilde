package com.example.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReaderRepository {

    private final Context context;
    private final SharedPreferences prefs;

    private static final String PREF_NAME = "mon_appel_prefs";
    private static final String KEY_CUSTOM_TEXT = "custom_mon_appel_text";
    private static final String KEY_THEME = "theme_index";
    private static final String KEY_SIZE = "font_size";
    private static final String KEY_CHAPTER = "last_chapter";
    private static final String KEY_SCROLL = "last_scroll_y";
    private static final String KEY_SERIF = "use_serif";
    private static final String KEY_BOOKMARKS = "bookmarks_set";

    private static final String KEY_LANGUAGE = "selected_language";

    private static ReaderRepository instance;

    public ReaderRepository(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized ReaderRepository get(Context context) {
        if (instance == null) {
            instance = new ReaderRepository(context);
        }
        return instance;
    }

    public String getLanguage() {
        return prefs.getString(KEY_LANGUAGE, "fr");
    }

    public void setLanguage(String langCode) {
        prefs.edit().putString(KEY_LANGUAGE, langCode).apply();
    }

    public String loadRawText() {
        String lang = getLanguage();
        String assetName;
        switch (lang) {
            case "ln": assetName = "mon_appel_ln.txt"; break;
            case "en": assetName = "mon_appel_en.txt"; break;
            case "pt": assetName = "mon_appel_pt.txt"; break;
            case "es": assetName = "mon_appel_es.txt"; break;
            default: assetName = "mon_appel.txt"; break;
        }

        String saved = prefs.getString(KEY_CUSTOM_TEXT + "_" + lang, null);
        if (saved != null && !saved.trim().isEmpty()) {
            return saved;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open(assetName), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "# Mon Appel\nBienvenue dans l'application Mon Appel.";
        }
    }

    public void saveRawText(String text) {
        prefs.edit().putString(KEY_CUSTOM_TEXT, text).apply();
    }

    public void resetToDefaultText() {
        prefs.edit().remove(KEY_CUSTOM_TEXT).apply();
    }

    public List<Chapter> getChapters() {
        String text = loadRawText();
        List<Chapter> list = new ArrayList<>();
        String currentTitle = "Introduction";
        StringBuilder sb = new StringBuilder();
        int idCounter = 0;

        String[] lines = text.split("\r?\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("# ")) {
                String body = formatBody(sb.toString().trim());
                if (!body.isEmpty()) {
                    list.add(new Chapter(idCounter++, currentTitle, body));
                }
                sb.setLength(0);
                currentTitle = trimmed.substring(2).trim();
            } else {
                sb.append(line).append("\n");
            }
        }
        String finalBody = formatBody(sb.toString().trim());
        if (!finalBody.isEmpty()) {
            list.add(new Chapter(idCounter++, currentTitle, finalBody));
        }

        if (list.isEmpty()) {
            list.add(new Chapter(0, "Contenu manquant", "Veuillez ajouter le texte dans mon_appel.txt."));
        }
        return list;
    }

    private String formatBody(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        String[] paragraphs = raw.split("\n\\s*\n");
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String p : paragraphs) {
            String cleaned = p.replaceAll("\\s*\n\\s*", " ").trim();
            if (!cleaned.isEmpty()) {
                if (!first) {
                    sb.append("\n\n");
                }
                sb.append(cleaned);
                first = false;
            }
        }
        return sb.toString();
    }

    public List<SearchResult> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<Chapter> chapters = getChapters();
        List<SearchResult> results = new ArrayList<>();
        String lowercaseQuery = query.toLowerCase().trim();

        for (int chIdx = 0; chIdx < chapters.size(); chIdx++) {
            Chapter ch = chapters.get(chIdx);
            String bodyLower = ch.getBody().toLowerCase();
            int startIndex = 0;
            while (true) {
                int found = bodyLower.indexOf(lowercaseQuery, startIndex);
                if (found == -1) break;

                int snippetStart = Math.max(0, found - 40);
                int snippetEnd = Math.min(ch.getBody().length(), found + query.length() + 60);
                String snippet = ch.getBody().substring(snippetStart, snippetEnd).replace("\n", " ");
                if (snippetStart > 0) snippet = "..." + snippet;
                if (snippetEnd < ch.getBody().length()) snippet = snippet + "...";

                results.add(new SearchResult(chIdx, ch.getTitle(), snippet, found));
                startIndex = found + lowercaseQuery.length() + 50;
            }
        }
        return results;
    }

    public int getThemeIndex() {
        return Math.max(0, Math.min(3, prefs.getInt(KEY_THEME, 0)));
    }

    public void setThemeIndex(int index) {
        prefs.edit().putInt(KEY_THEME, index).apply();
    }

    public float getFontSize() {
        float size = prefs.getFloat(KEY_SIZE, 19f);
        return Math.max(12f, Math.min(34f, size));
    }

    public void setFontSize(float size) {
        prefs.edit().putFloat(KEY_SIZE, size).apply();
    }

    public int getLastChapter() {
        return prefs.getInt(KEY_CHAPTER, 0);
    }

    public void setLastChapter(int chapter) {
        prefs.edit().putInt(KEY_CHAPTER, chapter).apply();
    }

    public int getLastScrollY() {
        return prefs.getInt(KEY_SCROLL, 0);
    }

    public void setLastScrollY(int scrollY) {
        prefs.edit().putInt(KEY_SCROLL, scrollY).apply();
    }

    public boolean getUseSerif() {
        return prefs.getBoolean(KEY_SERIF, true);
    }

    public void setUseSerif(boolean serif) {
        prefs.edit().putBoolean(KEY_SERIF, serif).apply();
    }

    public boolean hasProgress() {
        return prefs.contains(KEY_CHAPTER);
    }

    public Set<String> getBookmarks() {
        Set<String> saved = prefs.getStringSet(KEY_BOOKMARKS, new HashSet<>());
        return saved != null ? new HashSet<>(saved) : new HashSet<>();
    }

    public boolean toggleBookmark(int chapterIndex) {
        Set<String> current = getBookmarks();
        String key = "ch_" + chapterIndex;
        boolean isAdded;
        if (current.contains(key)) {
            current.remove(key);
            isAdded = false;
        } else {
            current.add(key);
            isAdded = true;
        }
        prefs.edit().putStringSet(KEY_BOOKMARKS, current).apply();
        return isAdded;
    }
}
