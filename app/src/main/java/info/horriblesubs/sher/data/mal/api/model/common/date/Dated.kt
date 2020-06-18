package info.horriblesubs.sher.data.mal.api.model.common.date

import com.google.gson.annotations.SerializedName

data class Dated (
    @SerializedName("string") var string: String? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("prop") var prop: Prop? = null,
    @SerializedName("to") var to: String? = null
)