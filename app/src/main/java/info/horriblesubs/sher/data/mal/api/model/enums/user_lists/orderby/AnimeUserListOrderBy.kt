package info.horriblesubs.sher.data.mal.api.model.enums.user_lists.orderby

enum class AnimeUserListOrderBy(private val type: String) : UserListOrderBy {
    TITLE("title"), FINISH_DATE("finish_date"), START_DATE("start_date"), SCORE("score"),
    LAST_UPDATED("last_updated"), TYPE("type"), RATED("rated"), REWATCH("rewatch"),
    PRIORITY("priority"), PROGRESS("progress"), STORAGE("storage"), AIR_START("air_start"),
    AIR_END("air_end"), STATUS("status");

    override fun toString(): String = type
}