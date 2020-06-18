package info.horriblesubs.sher.data.mal.api.model.main.anime.episodes

import com.google.gson.annotations.SerializedName

data class AnimeEpisodeItem(
    @SerializedName("title_japanese") var titleJapanese: String? = null,
    @SerializedName("title_romanji") var titleRomanji: String? = null,
    @SerializedName("video_url") var videoUrl: String? = null,
    @SerializedName("forum_url") var forumUrl: String? = null,
    @SerializedName("episode_id") var episodeId: Int? = null,
    @SerializedName("filler") var filler: Boolean? = null,
    @SerializedName("recap") var recap: Boolean? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("aired") var aired: String? = null
)