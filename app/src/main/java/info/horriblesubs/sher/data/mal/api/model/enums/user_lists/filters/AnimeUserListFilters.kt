package info.horriblesubs.sher.data.mal.api.model.enums.user_lists.filters

enum class AnimeUserListFilters(private val type: String): UserListFilters {
    ALL("all"), WATCHING("watching"), COMPLETED("completed"), ON_HOLD("onhold"),
    DROPPED("dropped"), PLAN_TO_WATCH("plantowatch");
    override fun toString(): String = type
}