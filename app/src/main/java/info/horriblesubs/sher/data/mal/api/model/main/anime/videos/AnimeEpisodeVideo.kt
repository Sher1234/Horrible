package info.horriblesubs.sher.data.mal.api.model.main.anime.videos

import com.google.gson.annotations.SerializedName

data class AnimeEpisodeVideo(
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("episode") var episode: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("url") var url: String? = null
)