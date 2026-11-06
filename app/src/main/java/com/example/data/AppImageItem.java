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

    public String getResName() { return resName; }
    public String getTitle() { return title; }
    public String getCaption() { return caption; }
    public String getDetails() { return details; }

    public static List<AppImageItem> getAllApkImages() {
        return getAllApkImages("fr");
    }

    public static List<AppImageItem> getAllApkImages(String lang) {
        List<AppImageItem> list = new ArrayList<>();

        if ("ln".equals(lang)) {
            list.add(new AppImageItem("img_dizolele_25ans", "Profete DIZOLELE-MPUNGU (mibu 25)", "Na mibu 25 na ntango ya bimonaneli ya liboso (1955).", "Elili ya Isaac Dizolele-Mpungu Wafiduswa na mibu 25. Kuna na Camp Teli azwaki bimonaneli mpe bokambi ya Nzambe mpo na KCC."));
            list.add(new AppImageItem("img_mvika_michel", "MVIKA Michel", "MVIKA Michel, moto ayambaki ngai na Camp Teli.", "Moninga ya pembeni ya Profete. Ayambaki ye na FORAKAM na Sanza ya Mibale 1955."));
            list.add(new AppImageItem("img_voni_lusala", "Mama VONI LUSALA Rosalie", "Mama VONI LUSALA Rosalie, mwasi ya MVIKA Michel.", "Mwasi ya MVIKA Michel. Asalisaki Profete na botangi mpe kosamba na Camp Teli."));
            list.add(new AppImageItem("img_maneka_albert", "MANEKA Albert", "Tatá na ngai MANEKA Albert.", "Tatá ya boboti ya Profete DIZOLELE-MPUNGU Wafiduswa Isaac."));
            list.add(new AppImageItem("img_mbambukulu_victor", "MBAMBUKULU Victor", "MBAMBUKULU Victor, yaya na ngai.", "Yaya ya Profete, abatelaki libota na nzela ya Nzambe."));
            list.add(new AppImageItem("img_ndonda_joel", "NDONDA Joël", "Ndeko na ngai NDONDA Joël.", "Cousin ya Profete, tatoli ya masolo ya ebandeli ya KCC."));
            list.add(new AppImageItem("img_nsakala_samuel", "Rév. NSAKALA NSONGI Samuel", "Rév. NSAKALA NSONGI Samuel, « Muana Mupepe ».", "Moyebi wa Nzambe « Muana Mupepe », asalisaki mingi na kobimisa sango ya KCC."));
            list.add(new AppImageItem("img_kifulu", "KIFULU", "KIFULU na mobu 2010.", "Mboka ya KIFULU na 2010, esika ya ntina na masolo ya KCC."));
            list.add(new AppImageItem("img_kimpungu", "KIMPUNGU", "Mboka KIMPUNGU na mobu 2010.", "Mboka KIMPUNGU na 2010, esika ya losambo ya KCC."));
            list.add(new AppImageItem("kcc_logo", "Elembo ya KCC", "Elembo ya KCC (Nkanda Wambote Wampa).", "Elembo ya Nzambe, liloba ya sika mpe kobikiswa ya milimo na KCC."));
        } else if ("en".equals(lang)) {
            list.add(new AppImageItem("img_dizolele_25ans", "Prophet DIZOLELE-MPUNGU (Age 25)", "At age 25 at the time of the apparitions (March 1955).", "Portrait of Isaac Dizolele-Mpungu Wafiduswa at age 25. During this time at Camp Teli he received divine visions and his calling to establish the Good New Message (KCC)."));
            list.add(new AppImageItem("img_mvika_michel", "MVIKA Michel", "MVIKA Michel, the man who hosted me at Camp Teli.", "Close friend of Prophet Dizolele-Mpungu. He hosted him at FORAKAM in February 1955 during his job search."));
            list.add(new AppImageItem("img_voni_lusala", "Mrs. VONI LUSALA Rosalie", "Mrs. VONI LUSALA Rosalie, wife of MVIKA Michel.", "Wife of MVIKA Michel. She provided hospitality and fraternal support to Isaac Dizolele-Mpungu during his spiritual retreat in March 1955."));
            list.add(new AppImageItem("img_maneka_albert", "MANEKA Albert", "My father MANEKA Albert.", "Biological father of Prophet DIZOLELE-MPUNGU Wafiduswa Isaac."));
            list.add(new AppImageItem("img_mbambukulu_victor", "MBAMBUKULU Victor", "MBAMBUKULU Victor, one of my elder brothers.", "Elder brother of the Prophet, a key figure in the prophetic family history."));
            list.add(new AppImageItem("img_ndonda_joel", "NDONDA Joël", "My cousin NDONDA Joël.", "Cousin of Isaac Dizolele-Mpungu, witness to the early spiritual milestones."));
            list.add(new AppImageItem("img_nsakala_samuel", "Rev. NSAKALA NSONGI Samuel", "Rev. NSAKALA NSONGI Samuel, 'Muana Mupepe'.", "Prominent minister known as 'Muana Mupepe', a pillar in spreading KCC doctrine."));
            list.add(new AppImageItem("img_kifulu", "KIFULU Locality", "Partial view of KIFULU locality in 2010.", "Historical view of KIFULU locality in 2010, connected to the Prophet's early journey."));
            list.add(new AppImageItem("img_kimpungu", "KIMPUNGU Locality", "KIMPUNGU locality in 2010.", "View of KIMPUNGU locality in 2010, an important site in KCC spiritual heritage."));
            list.add(new AppImageItem("kcc_logo", "KCC Emblem", "Official logo of the Good New Message (KCC).", "Official emblem representing salvation, prophetic faith, and divine light in KCC."));
        } else if ("pt".equals(lang)) {
            list.add(new AppImageItem("img_dizolele_25ans", "Profeta DIZOLELE-MPUNGU (25 anos)", "Aos 25 anos de idade na altura das aparições (Março de 1955).", "Retrato de Isaac Dizolele-Mpungu Wafiduswa aos 25 anos no Camp Teli quando recebeu o chamamento divino."));
            list.add(new AppImageItem("img_mvika_michel", "MVIKA Michel", "MVIKA Michel, o homem que me acolheu no Camp Teli.", "Amigo próximo do Profeta que o recebeu na FORAKAM em fevereiro de 1955."));
            list.add(new AppImageItem("img_voni_lusala", "Sra. VONI LUSALA Rosalie", "Senhora VONI LUSALA Rosalie, esposa de MVIKA Michel.", "Esposa de MVIKA Michel, apoiou fraternalmente o Profeta durante os dias de visões."));
            list.add(new AppImageItem("img_maneka_albert", "MANEKA Albert", "Meu pai MANEKA Albert.", "Pai biológico do Profeta DIZOLELE-MPUNGU Wafiduswa Isaac."));
            list.add(new AppImageItem("img_mbambukulu_victor", "MBAMBUKULU Victor", "MBAMBUKULU Victor, um dos meus irmãos mais velhos.", "Irmão mais velho do Profeta, figura respeitada na família."));
            list.add(new AppImageItem("img_ndonda_joel", "NDONDA Joël", "Meu primo NDONDA Joël.", "Primo do Profeta, testemunha dos primeiros momentos do ministério."));
            list.add(new AppImageItem("img_nsakala_samuel", "Rev. NSAKALA NSONGI Samuel", "Rev. NSAKALA NSONGI Samuel, 'Muana Mupepe'.", "Ministro da Igreja apelidado 'Muana Mupepe', pilar na difusão da mensagem KCC."));
            list.add(new AppImageItem("img_kifulu", "Localidade de KIFULU", "Vista parcial de KIFULU em 2010.", "Vista histórica da localidade de KIFULU em 2010."));
            list.add(new AppImageItem("img_kimpungu", "Localidade de KIMPUNGU", "Localidade de KIMPUNGU em 2010.", "Vista da localidade de KIMPUNGU em 2010, local marcante no património KCC."));
            list.add(new AppImageItem("kcc_logo", "Emblema KCC", "Logótipo oficial da Boa Nova Mensagem (KCC).", "Símbolo oficial que representa a salvação e a luz divina da Igreja KCC."));
        } else if ("es".equals(lang)) {
            list.add(new AppImageItem("img_dizolele_25ans", "Profeta DIZOLELE-MPUNGU (25 años)", "A los 25 años en el momento de las apariciones (Marzo de 1955).", "Retrato de Isaac Dizolele-Mpungu Wafiduswa a los 25 años al recibir su llamado profético en Camp Teli."));
            list.add(new AppImageItem("img_mvika_michel", "MVIKA Michel", "MVIKA Michel, el hombre que me hospedó en Camp Teli.", "Amigo cercano del Profeta que lo acogió en FORAKAM en febrero de 1955."));
            list.add(new AppImageItem("img_voni_lusala", "Sra. VONI LUSALA Rosalie", "Señora VONI LUSALA Rosalie, esposa de MVIKA Michel.", "Esposa de MVIKA Michel, brindó apoyo fraternal durante los días de retiro espiritual."));
            list.add(new AppImageItem("img_maneka_albert", "MANEKA Albert", "Mi padre MANEKA Albert.", "Padre biológico del Profeta DIZOLELE-MPUNGU Wafiduswa Isaac."));
            list.add(new AppImageItem("img_mbambukulu_victor", "MBAMBUKULU Victor", "MBAMBUKULU Victor, uno de mis hermanos mayores.", "Hermano mayor del Profeta y pilar en la familia."));
            list.add(new AppImageItem("img_ndonda_joel", "NDONDA Joël", "Mi primo NDONDA Joël.", "Primo del Profeta y testigo de los primeros eventos espirituales."));
            list.add(new AppImageItem("img_nsakala_samuel", "Rev. NSAKALA NSONGI Samuel", "Rev. NSAKALA NSONGI Samuel, 'Muana Mupepe'.", "Destacado reverendo apodado 'Muana Mupepe', fundamental en la difusión del mensaje KCC."));
            list.add(new AppImageItem("img_kifulu", "Localidad KIFULU", "Vista parcial de KIFULU en 2010.", "Vista de la localidad histórica de KIFULU en 2010."));
            list.add(new AppImageItem("img_kimpungu", "Localidad KIMPUNGU", "Localidad KIMPUNGU en 2010.", "Vista de la localidad de KIMPUNGU en 2010, lugar emblemático en la historia de KCC."));
            list.add(new AppImageItem("kcc_logo", "Emblema KCC", "Logotipo oficial del Buen Nuevo Mensaje (KCC).", "Insignia oficial que representa la salvación de las almas y la palabra divina."));
        } else { // "fr" default
            list.add(new AppImageItem("img_dizolele_25ans", "Prophète DIZOLELE-MPUNGU (à 25 ans)", "À l’âge de 25 ans au moment des apparitions (Mars 1955).", "Portrait d'Isaac Dizolele-Mpungu Wafiduswa à l'âge de 25 ans. C'est durant cette période au Camp Teli qu'il reçut les révélations prophétiques et l'appel direct de l'Éternel Dieu pour accomplir sa mission et transmettre le Bon Nouveau Message (KCC)."));
            list.add(new AppImageItem("img_mvika_michel", "MVIKA Michel", "MVIKA Michel, l’homme qui m’a reçu et hébergé au Camp Teli.", "Ami proche du Prophète DIZOLELE-MPUNGU. En février 1955, alors qu'Isaac recherchait du travail, MVIKA Michel l'a invité à FORAKAM et l'a logé au Camp Teli, devenant ainsi un témoin direct des événements spirituels du début du ministère."));
            list.add(new AppImageItem("img_voni_lusala", "Mme VONI LUSALA Rosalie", "Madame VONI LUSALA Rosalie, épouse de MVIKA Michel.", "Épouse de MVIKA Michel. Elle a offert l'hospitalité et l'assistance fraternelle à Isaac Dizolele-Mpungu au Camp Teli lors des jours d'apparitions et de jeûne spirituel."));
            list.add(new AppImageItem("img_maneka_albert", "MANEKA Albert", "Mon père MANEKA Albert.", "Père géniteur du Prophète DIZOLELE-MPUNGU Wafiduswa Isaac, figure paternelle respectée."));
            list.add(new AppImageItem("img_mbambukulu_victor", "MBAMBUKULU Victor", "MBAMBUKULU Victor, l’un de mes frères aînés.", "Frère aîné du Prophète, membre de la famille prophétique ayant accompagné et soutenu la marche spirituelle du KCC."));
            list.add(new AppImageItem("img_ndonda_joel", "NDONDA Joël", "Mon cousin NDONDA Joël.", "Cousin de Papa Isaac Dizolele-Mpungu, témoin des événements historiques et de la croissance de l'œuvre spirituelle."));
            list.add(new AppImageItem("img_nsakala_samuel", "Rév. NSAKALA NSONGI Samuel", "Rév. NSAKALA NSONGI Samuel, « Muana Mupepe ».", "Figure éminente et serviteur de Dieu surnommé 'Muana Mupepe', ayant joué un rôle capital dans le développement et l'édification de la communauté du Bon Nouveau Message."));
            list.add(new AppImageItem("img_kifulu", "Localité KIFULU", "Vue partielle de la Localité KIFULU en 2010.", "Paysage et vue partielle de la localité historique de KIFULU en 2010, berceau de faits spirituels mémorables retracés dans l'ouvrage."));
            list.add(new AppImageItem("img_kimpungu", "Localité KIMPUNGU", "La localité KIMPUNGU en 2010.", "Vue de la localité de KIMPUNGU en 2010, un des hauts lieux de mémoire du ministère prophétique et de l'histoire du KCC."));
            list.add(new AppImageItem("kcc_logo", "Emblème KCC", "Logo officiel de l'Église Bon Nouveau Message (KCC).", "Symbole officiel du Nkanda Wambote Wampa (Bon Nouveau Message), représentant le salut des âmes, la foi prophétique et la lumière divine."));
        }

        return list;
    }
}
