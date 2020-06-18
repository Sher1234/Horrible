package info.horriblesubs.sher.data.mal.api.model.common.user

import info.horriblesubs.sher.data.mal.api.model.common.date.DateProp
import info.horriblesubs.sher.data.mal.api.model.enums.AnimeSeason
import info.horriblesubs.sher.data.mal.api.model.enums.search.Sort
import info.horriblesubs.sher.data.mal.api.model.enums.user_lists.filters.AnimeUserListFilters
import info.horriblesubs.sher.data.mal.api.model.enums.user_lists.filters.MangaUserListFilters
import info.horriblesubs.sher.data.mal.api.model.enums.user_lists.filters.UserListFilters
import info.horriblesubs.sher.data.mal.api.model.enums.user_lists.orderby.AnimeUserListOrderBy
import info.horriblesubs.sher.data.mal.api.model.enums.user_lists.orderby.MangaUserListOrderBy
import info.horriblesubs.sher.data.mal.api.model.enums.user_lists.orderby.UserListOrderBy
import info.horriblesubs.sher.data.mal.api.model.enums.user_lists.status.AnimeUserListStatus
import info.horriblesubs.sher.data.mal.api.model.enums.user_lists.status.MangaUserListStatus
import info.horriblesubs.sher.data.mal.api.model.enums.user_lists.status.UserListStatus

abstract class AdvancedUserList {
    abstract var orderBy1: UserListOrderBy?
    abstract var orderBy2: UserListOrderBy?
    abstract var filter: UserListFilters?
    abstract var status: UserListStatus?

    //TODO: Date yyyy-00-00 yyyy-mm-00 yyyy-mm-dd
    var startDate: DateProp? = null
    var endDate: DateProp? = null

    var sort: Sort? = null

    var search: String? = null
    var page: Int = 1
        set(value) {
            field = if (value < 1) 1 else value
        }

    open fun get(): Map<String, String> {
        val hashMap = hashMapOf<String, String>()
        hashMap["search"] = search?:""
        hashMap["page"] = page.toString()
        hashMap["sort"] = sort?.toString()?:""
        hashMap["order_by"] = orderBy1?.toString()?:""
        hashMap["order_by2"] = orderBy2?.toString()?:""
        return hashMap
    }

    class Anime: AdvancedUserList() {
        override var filter: UserListFilters? = AnimeUserListFilters.ALL
            set(value) {
                field = if (value is AnimeUserListFilters) value else AnimeUserListFilters.ALL
            }

        override var orderBy1: UserListOrderBy? = null
            set(value) {
                field = if (value is AnimeUserListOrderBy) value else null
            }

        override var orderBy2: UserListOrderBy? = null
            set(value) {
                field = if (value is AnimeUserListOrderBy) value else null
            }

        override var status: UserListStatus? = null
            set(value) {
                field = if (value is AnimeUserListStatus) value else null
            }

        var producer: Int? = null
            set(value) {
                field = if (value != null && value > 0) value else null
            }

        var year: Int? = null
            set(value) {
                field = if (value != null && value > 1800) value else null
            }

        var season: AnimeSeason? = null

        override fun get(): Map<String, String> {
            val hashMap = hashMapOf<String, String>()
            hashMap["aired_from"] = startDate?.toString()?:""
            hashMap["aired_to"] = endDate?.toString()?:""
            hashMap["producer"] = producer?.toString()?:""
            hashMap["year"] = year?.toString()?:""
            hashMap["season"] = season?.toString()?:""
            hashMap["airing_status"] = status?.toString()?:""
            hashMap.putAll(super.get())
            return hashMap.filterValues {!it.isBlank()}
        }
    }

    class Manga: AdvancedUserList() {
        override var filter: UserListFilters? = MangaUserListFilters.ALL
            set(value) {
                field = if (value is MangaUserListFilters) value else MangaUserListFilters.ALL
            }

        override var orderBy1: UserListOrderBy? = null
            set(value) {
                field = if (value is MangaUserListOrderBy) value else null
            }

        override var orderBy2: UserListOrderBy? = null
            set(value) {
                field = if (value is MangaUserListOrderBy) value else null
            }

        override var status: UserListStatus? = null
            set(value) {
                field = if (value is MangaUserListStatus) value else null
            }

        var magazine: Int? = null
            set(value) {
                field = if (value != null && value > 0) value else null
            }

        override fun get(): Map<String, String> {
            val hashMap = hashMapOf<String, String>()
            hashMap["published_from"] = startDate?.toString()?:""
            hashMap["published_to"] = endDate?.toString()?:""
            hashMap["magazine"] = magazine?.toString()?:""
            hashMap["publishing_status"] = status?.toString()?:""
            hashMap.putAll(super.get())
            return hashMap.filterValues {!it.isBlank()}
        }
    }
}