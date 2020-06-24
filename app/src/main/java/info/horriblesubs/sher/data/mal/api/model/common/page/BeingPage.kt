package info.horriblesubs.sher.data.mal.api.model.common.page

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithImage

open class BeingPage(
    @SerializedName("member_favorites") var memberFavorites: Int? = null,
    @SerializedName("about") var about: String? = null,
    imageUrl: String? = null,
    name: String? = null,
    url: String? = null,
    malId: Int? = null,
): BaseWithImage(imageUrl = imageUrl, malId = malId, name = name, url = url)