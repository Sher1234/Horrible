package info.horriblesubs.sher.data.mal.api.model.enums.search.type


enum class MangaSearchTypes(private val type: String): AdvancedSearchTypes {
    MANGA("manga"), NOVEL("novel"), ONESHOT("oneshot"), DOUJIN("doujin"),
    MANHWA("manhwa"), MANHUA("manhua");

    override fun toString(): String = type
}