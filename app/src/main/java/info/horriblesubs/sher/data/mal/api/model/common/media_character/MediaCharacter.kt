package info.horriblesubs.sher.data.mal.api.model.common.media_character

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.api.mal.model.common.base.BaseWithImage

open class MediaCharacter(
    @SerializedName("role") var role: String? = null,
    imageUrl: String? = null,
    name: String? = null,
    url: String? = null,
    malId: Int? = null,
): BaseWithImage(imageUrl = imageUrl, malId = malId, name = name, url = url)