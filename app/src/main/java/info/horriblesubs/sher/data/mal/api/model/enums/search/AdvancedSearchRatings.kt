package info.horriblesubs.sher.data.mal.api.model.enums.search

enum class AdvancedSearchRatings(private val rating: String) {
    G("g"), PG("pg"), PG13("pg13"), R17("r17"), R("r"), RX("rx");

    override fun toString(): String = rating
}