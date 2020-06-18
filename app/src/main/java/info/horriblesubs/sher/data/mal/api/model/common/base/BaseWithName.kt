package info.horriblesubs.sher.data.mal.api.model.common.base

import com.google.gson.annotations.SerializedName

open class BaseWithName(
    @SerializedName("name") var name: String? = null,
    url: String? = null,
    malId: Int? = null,
): Base(malId = malId, url = url)