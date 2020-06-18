package info.horriblesubs.sher.data.mal.api.model.enums.user_lists.status

enum class AnimeUserListStatus(private val type: String) : UserListStatus {
    AIRING("airing"), FINISHED("finished"), TO_BE_AIRED("to_be_aired");
    override fun toString(): String = type
}