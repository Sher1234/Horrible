package info.horriblesubs.sher.data.mal.api.model.enums.user_lists.orderby

enum class MangaUserListOrderBy(private val type: String): UserListOrderBy {
    TITLE("title"), FINISH_DATE("finish_date"), START_DATE("start_date"), SCORE("score"),
    LAST_UPDATED("last_updated"), TYPE("type"), PRIORITY("priority"), PROGRESS("progress"),
    VOLUMES_READ("volumes_read"), PUBLISH_START("publish_start"),
    PUBLISH_END("publish_end"), STATUS("status");

    override fun toString(): String = type
}