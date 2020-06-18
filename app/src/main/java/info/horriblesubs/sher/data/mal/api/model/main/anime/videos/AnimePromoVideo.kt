package info.horriblesubs.sher.data.mal.api.model.main.anime.videos

import com.google.gson.annotations.SerializedName

data class AnimePromoVideo(
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("video_url") var videoUrl: String? = null,
    @SerializedName("title") var title: String? = null,
)