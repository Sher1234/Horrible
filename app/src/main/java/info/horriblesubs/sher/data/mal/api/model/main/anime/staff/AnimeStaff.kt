package info.horriblesubs.sher.data.mal.api.model.main.anime.staff

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.api.mal.model.common.base.BaseWithImage

class AnimeStaff(
    @SerializedName("positions") var positions: ArrayList<String>? = null,
    imageUrl: String? = null,
    name: String? = null,
    url: String? = null,
    malId: Int? = null
): BaseWithImage(imageUrl = imageUrl, malId = malId, name = name, url = url)