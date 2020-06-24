package info.horriblesubs.sher.data.mal.api.model.common.base

import com.google.gson.annotations.SerializedName

open class BaseWithType(
    @SerializedName("type") var type: String? = null,
    name: String? = null,
    url: String? = null,
    malId: Int? = null,
): BaseWithName(malId = malId, name = name, url = url) {
    override fun toString() = name ?: "Null"
}