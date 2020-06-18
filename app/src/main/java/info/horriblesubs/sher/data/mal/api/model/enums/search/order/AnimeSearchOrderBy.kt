package info.horriblesubs.sher.data.mal.api.model.enums.search.order


enum class AnimeSearchOrderBy(private val type: String):
    AdvancedSearchOrderBy {
    TITLE("title"), START_DATE("start_date"), END_DATE("end_date"),
    SCORE("score"), TYPE("type"), MEMBERS("members"), ID("id"),
    EPISODES("episodes"), RATING("rating");

    override fun toString(): String = type
}