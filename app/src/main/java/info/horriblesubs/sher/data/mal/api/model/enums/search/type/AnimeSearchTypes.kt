package info.horriblesubs.sher.data.mal.api.model.enums.search.type


enum class AnimeSearchTypes(private val type: String): AdvancedSearchTypes {
    TV("tv"), OVA("ova"), MOVIE("movie"), SPECIAL("special"), ONA("ona"),
    MUSIC("music");

    override fun toString(): String = type
}