package info.horriblesubs.sher.data.mal.api.model.main.anime.videos

import com.google.gson.annotations.SerializedName

data class AnimeVideosPage(
    @SerializedName("episodes") var episodes: List<AnimeEpisodeVideo>? = null,
    @SerializedName("promo") var promos: List<AnimePromoVideo>? = null,
)