package info.horriblesubs.sher.data.mal.api.model.common

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithImage

class VoiceActor (
    malId: Int? = null,
    url: String? = null,
    name: String? = null,
    imageUrl: String? = null,
    @SerializedName("language") var language: String? = null,
): BaseWithImage(imageUrl = imageUrl, name = name, url = url, malId = malId) {
    override fun toString(): String {
        return name ?: ""
    }
}