package info.horriblesubs.sher.data.mal.api.model.common.search

import info.horriblesubs.sher.data.mal.api.model.common.date.DateProp
import info.horriblesubs.sher.data.mal.api.model.enums.search.AdvancedSearchRatings
import info.horriblesubs.sher.data.mal.api.model.enums.search.Sort
import info.horriblesubs.sher.data.mal.api.model.enums.search.order.AdvancedSearchOrderBy
import info.horriblesubs.sher.data.mal.api.model.enums.search.order.AnimeSearchOrderBy
import info.horriblesubs.sher.data.mal.api.model.enums.search.order.MangaSearchOrderBy
import info.horriblesubs.sher.data.mal.api.model.enums.search.status.AdvancedSearchStatus
import info.horriblesubs.sher.data.mal.api.model.enums.search.status.AnimeSearchStatus
import info.horriblesubs.sher.data.mal.api.model.enums.search.status.MangaSearchStatus
import info.horriblesubs.sher.data.mal.api.model.enums.search.type.AdvancedSearchTypes
import info.horriblesubs.sher.data.mal.api.model.enums.search.type.AnimeSearchTypes
import info.horriblesubs.sher.data.mal.api.model.enums.search.type.MangaSearchTypes

abstract class AdvancedSearch {
    abstract var orderBy: AdvancedSearchOrderBy?
    abstract var status: AdvancedSearchStatus?
    abstract var type: AdvancedSearchTypes?

    var rating: AdvancedSearchRatings? = null
    var sort: Sort? = null

    //TODO: Date yyyy-00-00 yyyy-mm-00 yyyy-mm-dd
    var startDate: DateProp? = null
    var endDate: DateProp? = null

    var genres: List<Int>? = null
    var letter: String? = null
    var score: Float? = null

    var genreInclude = true

    open fun get(): Map<String, String> {
        val hashMap = hashMapOf<String, String>()
        hashMap["type"] = type?.toString()?:""
        hashMap["status"] = status?.toString()?:""
        hashMap["rated"] = rating?.toString()?:""
        hashMap["genre"] = genres?.joinToString(",")?:""
        hashMap["score"] = score?.toString()?:""
        hashMap["start_date"] = startDate?.toString()?:""
        hashMap["end_date"] = endDate?.toString()?:""
        hashMap["limit"] = "50"
        hashMap["order_by"] = orderBy?.toString()?:""
        hashMap["sort"] = sort?.toString()?:""
        hashMap["letter"] = letter?:""

        if (genreInclude) hashMap.remove("genre_exclude")
        else hashMap["genre_exclude"] = "0"

        return hashMap
    }

    class Anime: AdvancedSearch() {
        override var orderBy: AdvancedSearchOrderBy? = null
            set(value) {
                field = if (value is AnimeSearchOrderBy?) value else null
            }
        override var status: AdvancedSearchStatus? = null
            set(value) {
                field = if (value is AnimeSearchStatus?) value else null
            }
        override var type: AdvancedSearchTypes? = null
            set(value) {
                field = if (value is AnimeSearchTypes?) value else null
            }
        var producer: Int? = null
            set(value) {
                field = if (value != null && value > 0) value else null
            }
        override fun get(): Map<String, String> {
            val hashMap = hashMapOf<String, String>()
            hashMap["producer"] = producer?.toString()?:""
            hashMap.putAll(super.get())
            return hashMap.filterValues {!it.isBlank()}
        }
    }

    class Manga: AdvancedSearch() {
        override var orderBy: AdvancedSearchOrderBy? = null
            set(value) {
                field = if (value is MangaSearchOrderBy?) value else null
            }
        override var status: AdvancedSearchStatus? = null
            set(value) {
                field = if (value is MangaSearchStatus?) value else null
            }
        override var type: AdvancedSearchTypes? = null
            set(value) {
                field = if (value is MangaSearchTypes?) value else null
            }
        var magazine: Int? = null
            set(value) {
                field = if (value != null && value > 0) value else null
            }
        override fun get(): Map<String, String> {
            val hashMap = hashMapOf<String, String>()
            hashMap["magazine"] = magazine?.toString()?:""
            hashMap.putAll(super.get())
            return hashMap.filterValues {!it.isBlank()}
        }
    }
}