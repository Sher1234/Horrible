package info.horriblesubs.sher.data.mal.api.model.common.stats

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.stats.score.Score

open class Stats(
    @SerializedName("completed") var completed: Int? = null,
    @SerializedName("dropped") var dropped: Int? = null,
    @SerializedName("on_hold") var onHold: Int? = null,
    @SerializedName("scores") var score: Score? = null,
    @SerializedName("total") var total: Int? = null
)