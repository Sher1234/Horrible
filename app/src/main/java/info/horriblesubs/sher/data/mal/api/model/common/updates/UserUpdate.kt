package info.horriblesubs.sher.data.mal.api.model.common.updates

import com.google.gson.annotations.SerializedName

open class UserUpdate (
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("score") var scores: Int? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("url") var url: String? = null
)
