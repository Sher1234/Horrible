package info.horriblesubs.sher.data.mal.api.model.enums

enum class AnimeSeason(private val season: String) {
    SUMMER("summer"), SPRING("spring"), FALL("fall"), WINTER("winter");

    override fun toString(): String = season
}