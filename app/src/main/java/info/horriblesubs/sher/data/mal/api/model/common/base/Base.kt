package info.horriblesubs.sher.data.mal.api.model.common.base

import com.google.gson.annotations.SerializedName

open class Base(
    @SerializedName("mal_id") var malId: Int? = null,
    @SerializedName("url") var url: String? = null,
)