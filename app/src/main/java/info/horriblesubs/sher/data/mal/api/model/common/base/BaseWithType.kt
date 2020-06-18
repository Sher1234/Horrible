package info.horriblesubs.sher.data.mal.api.model.common.base

import com.google.gson.annotations.SerializedName

open class BaseWithType(
    @SerializedName("type") open val type: String?,
    name: String? = null,
    url: String? = null,
    malId: Int? = null,
): BaseWithName(malId = malId, name = name, url = url)