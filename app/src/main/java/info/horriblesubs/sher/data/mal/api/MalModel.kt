package info.horriblesubs.sher.data.mal.api

import info.horriblesubs.sher.data.mal.api.model.main.anime.AnimePage
import info.horriblesubs.sher.ui._extras.adapters.Info

inline val AnimePage.allTitles: String get() {
    val titleOthers = if (titleSynonyms.isNullOrEmpty()) "" else "<b>Other Titles:</b> ${titleSynonyms?.joinToString()}"
    val titleJapanese = if (this.titleJapanese.isNullOrBlank()) "" else "<b>Japanese Title:</b> ${this.titleJapanese}<br>"
    val titleEnglish = if (this.titleEnglish.isNullOrBlank()) "" else "<b>English Title:</b> ${this.titleEnglish}<br>"
    return (titleEnglish + titleJapanese + titleOthers).trim()
}

inline val AnimePage.infoList: List<Info> get() = listOf(
    Info("Episodes", episodes?.toString() ?: "?"),
    Info("Rank", if (rank == null || rank!! < 1) "Unranked" else "#$rank"),
    Info("Popularity", if (popularity == null || popularity!! < 1) "#0" else "#$popularity"),
    Info("Score", if (score == null || score!! < 1) "0.0" else score?.toString() ?: "0.0"),
    Info("Premiered", premiered ?: "NA"),
    Info("Type", type ?: "NA"),
    Info("Source", source ?: "NA"),
    Info("Rating", rating ?: "NA"),
    Info("Status", status ?: "NA"),
    Info("Duration", duration ?: "NA"),
    Info("Aired", aired?.string ?: "NA"),
    Info("Broadcast", broadcast ?: "NA"),
    Info("Favorites", if (favorites == null || favorites!! < 1) "#0" else "#$favorites"),
    Info("Scored By", if (scoredBy == null || scoredBy!! < 1) "#0" else "#$scoredBy"),
    Info("Members", if (members == null || members!! < 1) "#0" else "#$members")
)