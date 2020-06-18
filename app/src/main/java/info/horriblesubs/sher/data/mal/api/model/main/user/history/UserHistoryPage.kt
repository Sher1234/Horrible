package info.horriblesubs.sher.data.mal.api.model.main.user.history

import com.google.gson.annotations.SerializedName

data class UserHistoryPage (
    @SerializedName("history") var history: ArrayList<UserHistoryItem>? = null
)