package info.horriblesubs.sher.data.mal.api.model.main.user.friends

import com.google.gson.annotations.SerializedName

data class UserFriendItem (
    @SerializedName("friends_since") var friendsSince: String? = null,
    @SerializedName("last_online") var lastOnline: String? = null,
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("url") var url: String? = null
)