package info.horriblesubs.sher.data.mal.api.model.main.character

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType
import info.horriblesubs.sher.data.mal.api.model.common.search.BeingSearchItem

class CharacterSearchItem(
    @SerializedName("anime") var animes: ArrayList<BaseWithType>? = null,
    @SerializedName("manga") var mangas: ArrayList<BaseWithType>? = null,
    alternativeNames: ArrayList<String>? = null,
    imageUrl: String? = null,
    name: String? = null,
    url: String? = null,
    malId: Int? = null,
): BeingSearchItem(imageUrl = imageUrl, malId = malId, name = name, url = url, alternativeNames = alternativeNames)