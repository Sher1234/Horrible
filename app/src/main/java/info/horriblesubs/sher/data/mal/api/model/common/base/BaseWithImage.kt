package info.horriblesubs.sher.data.mal.api.model.common.base

import com.google.gson.annotations.SerializedName

open class BaseWithImage(
    @SerializedName("image_url") var imageUrl: String? = null,
    name: String? = null,
    url: String? = null,
    malId: Int? = null,
): BaseWithName(malId = malId, name = name, url = url)