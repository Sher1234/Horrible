package info.horriblesubs.sher.data.mal.api

import info.horriblesubs.sher.data.mal.api.model.main.anime.AnimePage
import info.horriblesubs.sher.ui._extras.adapters.InfoAdapter

inline val AnimePage.allTitles: String get() {
    val titleOthers = if (titleSynonyms.isNullOrEmpty()) "" else "<b>Other Titles:</b> ${titleSynonyms?.joinToString()}"
    val titleJapanese = if (this.titleJapanese.isNullOrBlank()) "" else "<b>Japanese Title:</b> ${this.titleJapanese}<br>"
    val titleEnglish = if (this.titleEnglish.isNullOrBlank()) "" else "<b>English Title:</b> ${this.titleEnglish}<br>"
    return (titleEnglish + titleJapanese + titleOthers).trim()
}

inline val AnimePage.infoList: List<InfoAdapter.Info> get() = listOf(
    InfoAdapter.Info("Episodes", episodes?.toString() ?: "?"),
    InfoAdapter.Info("Rank", if (rank == null || rank!! < 1) "Unranked" else "#$rank"),
    InfoAdapter.Info("Popularity", if (popularity == null || popularity!! < 1) "#0" else "#$popularity"),
    InfoAdapter.Info("Score", if (score == null || score!! < 1) "0.0" else score?.toString() ?: "0.0"),
    InfoAdapter.Info("Premiered", premiered ?: "NA"),
    InfoAdapter.Info("Type", type ?: "NA"),
    InfoAdapter.Info("Source", source ?: "NA"),
    InfoAdapter.Info("Rating", rating ?: "NA"),
    InfoAdapter.Info("Status", status ?: "NA"),
    InfoAdapter.Info("Duration", duration ?: "NA"),
    InfoAdapter.Info("Aired", aired?.string ?: "NA"),
    InfoAdapter.Info("Broadcast", broadcast ?: "NA"),
    InfoAdapter.Info("Favorites", if (favorites == null || favorites!! < 1) "#0" else "#$favorites"),
    InfoAdapter.Info("Scored By", if (scoredBy == null || scoredBy!! < 1) "#0" else "#$scoredBy"),
    InfoAdapter.Info("Members", if (members == null || members!! < 1) "#0" else "#$members")
)