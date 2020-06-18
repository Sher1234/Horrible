package info.horriblesubs.sher.data.mal.api.model.common.date

import com.google.gson.annotations.SerializedName

data class DateProp(
    @SerializedName("month") var month: Int? = null,
    @SerializedName("year") var year: Int? = null,
    @SerializedName("day") var day: Int? = null
) {
    override fun toString(): String = "$year.-$month-$day"
}