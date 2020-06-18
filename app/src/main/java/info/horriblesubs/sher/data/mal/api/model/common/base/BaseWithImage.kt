package info.horriblesubs.sher.api.mal.model.common.base

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithName

open class BaseWithImage(
    @SerializedName("image_url") open var imageUrl: String? = null,
    name: String? = null,
    url: String? = null,
    malId: Int? = null,
): BaseWithName(malId = malId, name = name, url = url)