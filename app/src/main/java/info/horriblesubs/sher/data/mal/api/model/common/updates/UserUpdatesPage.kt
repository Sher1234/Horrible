package info.horriblesubs.sher.data.mal.api.model.common.updates

import com.google.gson.annotations.SerializedName

data class UserUpdatesPage<T: UserUpdate>(
    @SerializedName("users") var userUpdates: ArrayList<T>? = null
)