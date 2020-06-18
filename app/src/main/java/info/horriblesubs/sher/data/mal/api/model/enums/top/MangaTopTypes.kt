package info.horriblesubs.sher.data.mal.api.model.enums.top

enum class MangaTopTypes(private val type: String): TopSubType {
    MANGA("manga"), NOVELS("novels"), ONESHOTS("oneshots"), DOUJIN("doujin"),
    MANHWA("manhwa"), MANHUA("manhua"), BY_POPULARITY("bypopularity"),
    FAVORITE("favorite");

    override fun toString(): String = type
}