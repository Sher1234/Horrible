package info.horriblesubs.sher.data.mal.api.model.enums.search.order


enum class MangaSearchOrderBy(private val type: String):
    AdvancedSearchOrderBy {
    TITLE("title"), START_DATE("start_date"), END_DATE("end_date"),
    SCORE("score"), TYPE("type"), MEMBERS("members"), ID("id"),
    CHAPTERS("chapters"), VOLUMES("volumes");

    override fun toString(): String = type
}