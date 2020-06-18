package info.horriblesubs.sher.data.mal.api.model.common.date

import com.google.gson.annotations.SerializedName

data class Prop(
    @SerializedName("from") var from: DateProp? = null,
    @SerializedName("to") var to: DateProp? = null
)