package info.horriblesubs.sher.data.mal.api.model.main.genre

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.AnimeItem
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType

data class GenreAnimePage(
    @SerializedName("anime") var animes: ArrayList<AnimeItem>? = null,
    @SerializedName("mal_url") var genre: BaseWithType? = null,
    @SerializedName("item_count") var itemCount: Int? = null
)