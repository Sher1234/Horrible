package info.horriblesubs.sher.data.mal.api.model.enums

enum class HistoryTypes(val type: String) {
    ANIME("anime"), MANGA("manga");

    override fun toString(): String = type
}