package info.horriblesubs.sher.data.mal.api.model.main.user

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.Base
import info.horriblesubs.sher.data.mal.api.model.enums.HistoryTypes
import info.horriblesubs.sher.data.mal.api.model.main.user.stat.AnimeStat
import info.horriblesubs.sher.data.mal.api.model.main.user.stat.MangaStat

class UserPage (
    @SerializedName("manga_stats") var mangaStats: ArrayList<MangaStat>? = null,
    @SerializedName("anime_stats") var animeStats: ArrayList<AnimeStat>? = null,
    @SerializedName("last_online") var lastOnline: String? = null,
    @SerializedName("favorites") var favorites: Favorites? = null,
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("birthday") var birthday: String? = null,
    @SerializedName("location") var location: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("joined") var joined: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("about") var about: String? = null,
    url: String? = null
): Base(userId, url) {
    fun getHistory(type: HistoryTypes) {
        "/user/$username/history$type"
    }

    fun getFriends(page: Int) {
        "/user/$username/friends/$page"
    }

//    val animeListSearch: AnimeUserListingSearch
//        get() = AnimeUserListingSearch(username)
//
//    val mangaListSearch: MangaUserListingSearch
//        get() = MangaUserListingSearch(username)
}