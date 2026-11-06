package com.example.data;

public class SearchResult {
    private final int chapterIndex;
    private final String chapterTitle;
    private final String snippet;
    private final int matchIndex;

    public SearchResult(int chapterIndex, String chapterTitle, String snippet, int matchIndex) {
        this.chapterIndex = chapterIndex;
        this.chapterTitle = chapterTitle;
        this.snippet = snippet;
        this.matchIndex = matchIndex;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public String getSnippet() {
        return snippet;
    }

    public int getMatchIndex() {
        return matchIndex;
    }
}
