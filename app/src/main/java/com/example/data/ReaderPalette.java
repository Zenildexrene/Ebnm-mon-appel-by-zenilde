package com.example.data;

import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;

public class ReaderPalette {
    private final int id;
    private final String name;
    private final int bg;
    private final int fg;
    private final int bar;
    private final int accent;

    public ReaderPalette(int id, String name, int bg, int fg, int bar, int accent) {
        this.id = id;
        this.name = name;
        this.bg = bg;
        this.fg = fg;
        this.bar = bar;
        this.accent = accent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBg() {
        return bg;
    }

    public int getFg() {
        return fg;
    }

    public int getBar() {
        return bar;
    }

    public int getAccent() {
        return accent;
    }

    public static List<ReaderPalette> getPalettes() {
        List<ReaderPalette> list = new ArrayList<>();
        // Nuit
        list.add(new ReaderPalette(0, "Nuit", Color.parseColor("#121824"), Color.parseColor("#E0E6ED"), Color.parseColor("#1B2434"), Color.parseColor("#D4AF37")));
        // Jour
        list.add(new ReaderPalette(1, "Jour", Color.parseColor("#FAFAFA"), Color.parseColor("#1A1A1A"), Color.parseColor("#FFFFFF"), Color.parseColor("#8B0000")));
        // Sépia
        list.add(new ReaderPalette(2, "Sépia", Color.parseColor("#F4ECD8"), Color.parseColor("#4A3B32"), Color.parseColor("#E8DEC8"), Color.parseColor("#7A2021")));
        // Sombre
        list.add(new ReaderPalette(3, "Sombre", Color.parseColor("#000000"), Color.parseColor("#D0D0D0"), Color.parseColor("#161616"), Color.parseColor("#C5A059")));
        return list;
    }
}
