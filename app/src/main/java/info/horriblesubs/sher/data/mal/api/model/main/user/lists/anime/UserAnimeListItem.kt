package info.horriblesubs.sher.data.mal.api.model.main.user.lists.anime

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType

data class UserAnimeListItem(
    @SerializedName("video_url") var videoUrl: String? = null,
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("mal_id") var malId: Int? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("score") var score: Int? = null,
    @SerializedName("url") var url: String? = null,

    @SerializedName("watched_episodes") var watchedEpisodes: Int? = null,
    @SerializedName("watching_status") var watchingStatus: Int? = null,
    @SerializedName("total_episodes") var totalEpisodes: Int? = null,
    @SerializedName("airing_status") var airingStatus: Int? = null,
    @SerializedName("season_name") var seasonName: String? = null,
    @SerializedName("season_year") var seasonYear: String? = null,

    @SerializedName("has_episode_video") var hasEpisodeVideo: Boolean? = null,
    @SerializedName("has_promo_video") var hasPromoVideo: Boolean? = null,
    @SerializedName("is_rewatching") var isRewatching: Boolean? = null,
    @SerializedName("has_video") var hasVideo: Boolean? = null,

    @SerializedName("licensors") var licensors: ArrayList<BaseWithType>? = null,
    @SerializedName("studios") var studios: ArrayList<BaseWithType>? = null,
    @SerializedName("watch_start_date") var watchStartDate: String? = null,
    @SerializedName("watch_end_date") var watchEndDate: String? = null,
    @SerializedName("added_to_list") var addedToList: Boolean? = null,
    @SerializedName("start_date") var startDate: String? = null,
    @SerializedName("priority") var priority: String? = null,
    @SerializedName("end_date") var endDate: String? = null,
    @SerializedName("storage") var storage: String? = null,
    @SerializedName("rating") var rating: String? = null,
    @SerializedName("tags") var tags: String? = null,
    @SerializedName("days") var days: String? = null,
)