package info.horriblesubs.sher.data.mal.api.model.common

import com.google.gson.annotations.SerializedName

data class VoiceActor (
    @SerializedName("url") var url: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("mal_id") var malId: Int? = null,
    @SerializedName("language") var language: String? = null,
    @SerializedName("image_url") var imageUrl: String? = null,
)