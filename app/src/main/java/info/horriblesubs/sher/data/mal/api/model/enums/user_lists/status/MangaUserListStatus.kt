package info.horriblesubs.sher.data.mal.api.model.enums.user_lists.status

enum class MangaUserListStatus(private val type: String) : UserListStatus {
    PUBLISHING("publishing"), FINISHED("finished"), TO_BE_PUBLISHED("to_be_published");

    override fun toString(): String = type
}