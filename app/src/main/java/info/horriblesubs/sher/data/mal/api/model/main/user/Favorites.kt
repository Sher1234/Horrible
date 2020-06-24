package info.horriblesubs.sher.data.mal.api.model.main.user

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithImage

data class Favorites (
    @SerializedName("characters") var characters: ArrayList<BaseWithImage>? = null,
    @SerializedName("people") var peoples: ArrayList<BaseWithImage>? = null,
    @SerializedName("manga") var mangas: ArrayList<BaseWithImage>? = null,
    @SerializedName("anime") var animes: ArrayList<BaseWithImage>? = null
)