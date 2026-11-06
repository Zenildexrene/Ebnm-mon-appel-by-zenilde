package com.example.data;

import java.util.ArrayList;
import java.util.List;

public class ChapterItem {

    public enum Type {
        TEXT,
        IMAGE
    }

    private final Type type;
    private final String content;
    private final String resName;
    private final String caption;

    public static ChapterItem createText(String content) {
        return new ChapterItem(Type.TEXT, content, "", "");
    }

    public static ChapterItem createImage(String resName, String caption) {
        return new ChapterItem(Type.IMAGE, "", resName, caption);
    }

    private ChapterItem(Type type, String content, String resName, String caption) {
        this.type = type;
        this.content = content;
        this.resName = resName;
        this.caption = caption;
    }

    public Type getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getResName() {
        return resName;
    }

    public String getCaption() {
        return caption;
    }

    public static List<ChapterItem> parseChapterItems(String body) {
        List<ChapterItem> items = new ArrayList<>();
        if (body == null || body.trim().isEmpty()) {
            return items;
        }

        String[] paragraphs = body.split("\n\n");
        for (String p : paragraphs) {
            String trimmed = p.trim();
            if (trimmed.startsWith("[IMG:") && trimmed.endsWith("]")) {
                String inner = trimmed.substring(5, trimmed.length() - 1);
                String[] parts = inner.split("\\|", 2);
                String resName = parts.length > 0 ? parts[0].trim() : "";
                String caption = parts.length > 1 ? parts[1].trim() : "";
                if (!resName.isEmpty()) {
                    items.add(ChapterItem.createImage(resName, caption));
                }
            } else if (!trimmed.isEmpty()) {
                items.add(ChapterItem.createText(trimmed));
            }
        }
        return items;
    }
}
