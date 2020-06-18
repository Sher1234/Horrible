package info.horriblesubs.sher.data.mal.api.model.main.producer

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.AnimeItem
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType

data class ProducerPage (
    @SerializedName("anime") var animes: ArrayList<AnimeItem>? = null,
    @SerializedName("meta") var meta: BaseWithType? = null,
)