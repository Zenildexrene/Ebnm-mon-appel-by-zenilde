package com.example.data;

public class StringsProvider {

    public static String getTitle(String lang) {
        switch (lang) {
            case "ln": return "KOKAMBIO NA NGAI";
            case "en": return "MY CALL";
            case "pt": return "O MEU CHAMADO";
            case "es": return "MI LLAMADO";
            default: return "MON APPEL";
        }
    }

    public static String getSubtitle(String lang) {
        switch (lang) {
            case "ln": return "DIZOLELE-MPUNGU WAFIDUSWA Isaac\nPROFETE YA NZAMBE MOPELASI";
            case "en": return "DIZOLELE-MPUNGU WAFIDUSWA Isaac\nPROPHET OF THE LORD";
            case "pt": return "DIZOLELE-MPUNGU WAFIDUSWA Isaac\nPROFETA DO SENHOR";
            case "es": return "DIZOLELE-MPUNGU WAFIDUSWA Isaac\nPROFETA DEL SEÑOR";
            default: return "DIZOLELE-MPUNGU WAFIDUSWA Isaac\nPROPHÈTE DE L’ÉTERNEL";
        }
    }

    public static String getBtnStart(String lang, boolean hasProgress) {
        switch (lang) {
            case "ln": return hasProgress ? "Landa kotánga" : "Banda kotánga";
            case "en": return hasProgress ? "Resume Reading" : "Start Reading";
            case "pt": return hasProgress ? "Continuar Leitura" : "Iniciar Leitura";
            case "es": return hasProgress ? "Continuar Lectura" : "Iniciar Lectura";
            default: return hasProgress ? "Reprendre la lecture" : "Commencer la lecture";
        }
    }

    public static String getBtnChapters(String lang) {
        switch (lang) {
            case "ln": return "Etánda ya Biteni";
            case "en": return "Table of Contents";
            case "pt": return "Índice dos Capítulos";
            case "es": return "Índice de Capítulos";
            default: return "Table des Chapitres";
        }
    }

    public static String getBtnSearch(String lang) {
        switch (lang) {
            case "ln": return "Luka";
            case "en": return "Search";
            case "pt": return "Pesquisar";
            case "es": return "Buscar";
            default: return "Rechercher";
        }
    }

    public static String getBtnAbout(String lang) {
        switch (lang) {
            case "ln": return "Sango ya búku";
            case "en": return "About";
            case "pt": return "Sobre";
            case "es": return "Acerca de";
            default: return "À propos";
        }
    }

    public static String getBtnGallery(String lang) {
        switch (lang) {
            case "ln": return "Lolakaka ya bilili & Archives";
            case "en": return "Photo Gallery & Archives";
            case "pt": return "Galeria de Fotos & Arquivos";
            case "es": return "Galería de Fotos & Archivos";
            default: return "Galerie de Photos & Archives";
        }
    }

    public static String getLangLabel(String lang) {
        switch (lang) {
            case "ln": return "Lokóta ya búku";
            case "en": return "Book Language";
            case "pt": return "Idioma do Livro";
            case "es": return "Idioma del Libro";
            default: return "Langue de l'ouvrage";
        }
    }

    public static String getThemeLabel(String lang) {
        switch (lang) {
            case "ln": return "Lolenge ya bondeko";
            case "en": return "Reading Theme";
            case "pt": return "Tema de Leitura";
            case "es": return "Tema de Lectura";
            default: return "Thème de lecture";
        }
    }

    public static String getBtnPrev(String lang) {
        switch (lang) {
            case "ln": return "Elongoli";
            case "en": return "Previous";
            case "pt": return "Anterior";
            case "es": return "Anterior";
            default: return "Précédent";
        }
    }

    public static String getBtnNext(String lang) {
        switch (lang) {
            case "ln": return "Elandi";
            case "en": return "Next";
            case "pt": return "Próximo";
            case "es": return "Siguiente";
            default: return "Suivant";
        }
    }

    public static String getChapterSuffix(String lang) {
        switch (lang) {
            case "ln": return "Biteni";
            case "en": return "Chapters";
            case "pt": return "Capítulos";
            case "es": return "Capítulos";
            default: return "Chapitres";
        }
    }
}
