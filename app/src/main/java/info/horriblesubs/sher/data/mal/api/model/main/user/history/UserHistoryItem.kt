package info.horriblesubs.sher.data.mal.api.model.main.user.history

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType

data class UserHistoryItem (
    @SerializedName("increment") var increment: Int? = null,
    @SerializedName("meta") var meta: BaseWithType? = null,
    @SerializedName("date") var date: String? = null
)