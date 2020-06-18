package info.horriblesubs.sher.data.mal.api.model.main.anime.episodes

import com.google.gson.annotations.SerializedName

data class AnimeEpisodesPage(
    @SerializedName("episodes") var episodes: ArrayList<AnimeEpisodeItem>? = null,
    @SerializedName("episodes_last_page") var lastPage: Int? = null,
)