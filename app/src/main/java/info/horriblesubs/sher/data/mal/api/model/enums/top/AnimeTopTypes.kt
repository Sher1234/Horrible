package info.horriblesubs.sher.data.mal.api.model.enums.top

enum class AnimeTopTypes(private val type: String): TopSubType {
    AIRING("airing"), UPCOMING("upcoming"), TV("tv"), MOVIE("movie"),
    OVA("ova"), SPECIAL("special"), BY_POPULARITY("bypopularity"),
    FAVORITE("favorite");

    override fun toString(): String = type
}