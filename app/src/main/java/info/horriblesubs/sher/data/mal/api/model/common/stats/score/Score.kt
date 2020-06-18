package info.horriblesubs.sher.data.mal.api.model.common.stats.score

import com.google.gson.annotations.SerializedName


data class Score (
    @SerializedName("1") var one: Position? = null,
    @SerializedName("2") var two: Position? = null,
    @SerializedName("3") var three: Position? = null,
    @SerializedName("4") var four: Position? = null,
    @SerializedName("5") var five: Position? = null,
    @SerializedName("6") var six: Position? = null,
    @SerializedName("7") var seven: Position? = null,
    @SerializedName("8") var eight: Position? = null,
    @SerializedName("9") var nine: Position? = null,
    @SerializedName("10") var ten: Position? = null
)
