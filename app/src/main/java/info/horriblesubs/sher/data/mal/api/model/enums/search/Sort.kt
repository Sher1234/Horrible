package info.horriblesubs.sher.data.mal.api.model.enums.search

enum class Sort(private val type: String) {
    ASCENDING("ascending"), DESCENDING("descending");
    override fun toString(): String = type
}