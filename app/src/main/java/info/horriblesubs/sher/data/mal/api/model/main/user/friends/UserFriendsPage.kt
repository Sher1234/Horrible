package info.horriblesubs.sher.data.mal.api.model.main.user.friends

import com.google.gson.annotations.SerializedName

data class UserFriendsPage (
    @SerializedName("friends") var friends: ArrayList<UserFriendItem>? = null
)