package info.horriblesubs.sher.data.mal.api.model.enums.search.status


enum class MangaSearchStatus(private val status: String): AdvancedSearchStatus {
    TO_BE_PUBLISHED("to_be_published"),
    UPCOMING("to_be_published"),
    TBP("to_be_published"),
    COMPLETED("completed"),
    COMPLETE("completed"),
    AIRING("publishing");

    override fun toString(): String = status
}