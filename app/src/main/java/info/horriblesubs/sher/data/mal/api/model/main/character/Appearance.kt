package info.horriblesubs.sher.data.mal.api.model.main.character

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithImage

class Appearance (
    @SerializedName("role") var role: String? = null,
    imageUrl: String? = null,
    name: String? = null,
    url: String? = null,
    malId: Int? = null,
): BaseWithImage(imageUrl = imageUrl, malId = malId, name = name, url = url)