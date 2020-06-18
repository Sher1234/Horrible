package info.horriblesubs.sher.data.mal.api.model.main.magazine

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType

data class MagazinesPage (
    @SerializedName("meta") var meta: BaseWithType? = null,
    @SerializedName("manga") var magazines: List<Magazine>? = null
)