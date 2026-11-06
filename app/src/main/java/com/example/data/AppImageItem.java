package com.example.data;

import java.util.ArrayList;
import java.util.List;

public class AppImageItem {
    private final String resName;
    private final String title;
    private final String caption;
    private final String details;

    public AppImageItem(String resName, String title, String caption, String details) {
        this.resName = resName;
        this.title = title;
        this.caption = caption;
        this.details = details;
    }

    public String getResName() {
        return resName;
    }

    public String getTitle() {
        return title;
    }

    public String getCaption() {
        return caption;
    }

    public String getDetails() {
        return details;
    }

    public static List<AppImageItem> getAllApkImages() {
        List<AppImageItem> list = new ArrayList<>();

        list.add(new AppImageItem(
                "img_dizolele_25ans",
                "Prophète DIZOLELE-MPUNGU (à 25 ans)",
                "À l’âge de 25 ans au moment des apparitions (Mars 1955).",
                "Portrait d'Isaac Dizolele-Mpungu Wafiduswa à l'âge de 25 ans. C'est durant cette période au Camp Teli qu'il reçut les révélations prophétiques et l'appel direct de l'Éternel Dieu pour accomplir sa mission et transmettre le Bon Nouveau Message (KCC)."
        ));

        list.add(new AppImageItem(
                "img_mvika_michel",
                "MVIKA Michel",
                "MVIKA Michel, l’homme qui m’a reçu et hébergé au Camp Teli.",
                "Ami proche du Prophète DIZOLELE-MPUNGU. En février 1955, alors qu'Isaac recherchait du travail, MVIKA Michel l'a invité à FORAKAM et l'a logé au Camp Teli, devenant ainsi un témoin direct des événements spirituels du début du ministère."
        ));

        list.add(new AppImageItem(
                "img_voni_lusala",
                "Mme VONI LUSALA Rosalie",
                "Madame VONI LUSALA Rosalie, épouse de MVIKA Michel.",
                "Épouse de MVIKA Michel. Elle a offert l'hospitalité et l'assistance fraternelle à Isaac Dizolele-Mpungu au Camp Teli lors des jours d'apparitions et de jeûne spirituel."
        ));

        list.add(new AppImageItem(
                "img_maneka_albert",
                "MANEKA Albert",
                "Mon père MANEKA Albert.",
                "Père géniteur du Prophète DIZOLELE-MPUNGU Wafiduswa Isaac, figure paternelle respectée."
        ));

        list.add(new AppImageItem(
                "img_mbambukulu_victor",
                "MBAMBUKULU Victor",
                "MBAMBUKULU Victor, l’un de mes frères aînés.",
                "Frère aîné du Prophète, membre de la famille prophétique ayant accompagné et soutenu la marche spirituelle du KCC."
        ));

        list.add(new AppImageItem(
                "img_ndonda_joel",
                "NDONDA Joël",
                "Mon cousin NDONDA Joël.",
                "Cousin de Papa Isaac Dizolele-Mpungu, témoin des événements historiques et de la croissance de l'œuvre spirituelle."
        ));

        list.add(new AppImageItem(
                "img_nsakala_samuel",
                "Rév. NSAKALA NSONGI Samuel",
                "Rév. NSAKALA NSONGI Samuel, « Muana Mupepe ».",
                "Figure éminente et serviteur de Dieu surnommé 'Muana Mupepe', ayant joué un rôle capital dans le développement et l'édification de la communauté du Bon Nouveau Message."
        ));

        list.add(new AppImageItem(
                "img_kifulu",
                "Localité KIFULU",
                "Vue partielle de la Localité KIFULU en 2010.",
                "Paysage et vue partielle de la localité historique de KIFULU en 2010, berceau de faits spirituels mémorables retracés dans l'ouvrage."
        ));

        list.add(new AppImageItem(
                "img_kimpungu",
                "Localité KIMPUNGU",
                "La localité KIMPUNGU en 2010.",
                "Vue de la localité de KIMPUNGU en 2010, un des hauts lieux de mémoire du ministère prophétique et de l'histoire du KCC."
        ));

        list.add(new AppImageItem(
                "kcc_logo",
                "Emblème KCC",
                "Logo officiel de l'Église Bon Nouveau Message (KCC).",
                "Symbole officiel du Nkanda Wambote Wampa (Bon Nouveau Message), représentant le salut des âmes, la foi prophétique et la lumière divine."
        ));

        return list;
    }
}
