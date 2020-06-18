package info.horriblesubs.sher.data.mal.api.model.enums.search.status


enum class AnimeSearchStatus(private val status: String): AdvancedSearchStatus {
    COMPLETED("completed"),
    COMPLETE("complete"),
    UPCOMING("upcoming"),
    AIRING("airing"),
    TBA("tba");

    override fun toString(): String = status
}