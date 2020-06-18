package info.horriblesubs.sher.data.mal.api.model.enums.user_lists.filters

enum class MangaUserListFilters(private val type: String): UserListFilters {
    ALL("all"), READING("reading"), COMPLETED("completed"), ON_HOLD("onhold"),
    DROPPED("dropped"), PLAN_TO_READ("plantoread");
    override fun toString(): String = type
}
