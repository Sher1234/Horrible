package info.horriblesubs.sher.data.mal.api.model.main.person.position

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.api.mal.model.common.base.BaseWithImage

data class VoiceActingRole (
    @SerializedName("character") var character: BaseWithImage? = null,
    @SerializedName("anime") var anime: BaseWithImage? = null,
    @SerializedName("role") var role: String? = null
)