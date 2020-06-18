package info.horriblesubs.sher.data.mal.api.model.main.genre.manga

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType

data class GenreMangaPage(
    @SerializedName("manga") var animes: List<GenreMangaItem>? = null,
    @SerializedName("mal_url") var genre: BaseWithType? = null,
    @SerializedName("item_count") var itemCount: Int? = null
)