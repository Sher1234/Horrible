package info.horriblesubs.sher.api.nyaa.model;

public enum TorrentCategory {
    All("0_0", null, null),

    Anime("1_0", "Anime", null),
    Anime_AMV("1_1", "Anime", "Anime Music Video"),
    Anime_English("1_2", "Anime", "Translated (English)"),
    Anime_Non_English("1_3", "Anime", "Translated (Non English)"),
    Anime_Raw("1_4", "Anime", "Raw"),

    Audio("2_0", "Audio", null),
    Audio_Lossless("2_1", "Audio", "Lossless"),
    Audio_Lossy("2_2", "Audio", "Lossy"),

    Literature("3_0", "Literature", null),
    Literature_English("3_1", "Literature", "Translated (English)"),
    Literature_Non_English("3_2", "Literature", "Translated (Non English)"),
    Literature_Raw("3_3", "Literature", "Raw"),

    Live_Action("4_0", "Live Action", null),
    Live_Action_English("4_1", "Live Action", "Translated (English)"),
    Live_Action_PV("4_2", "Live Action", "Idol/Promotional Video"),
    Live_Action_Non_English("4_3", "Live Action", "Translated (Non English)"),
    Live_Action_Raw("4_4", "Live Action", "Raw"),

    Pictures("5_0", "Pictures", null),
    Pictures_Graphics("5_1", "Pictures", "Graphics"),
    Pictures_Photos("5_2", "Pictures", "Photos"),

    Software("6_0", "Software", null),
    Software_Applications("6_1", "Software", "Applications"),
    Software_Games("6_2", "Software", "Games");

    private final String id;
    private final String category;
    private final String subCategory;

    TorrentCategory(String id, String category, String subCategory) {
        this.subCategory = subCategory;
        this.category = category;
        this.id = id;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }
}