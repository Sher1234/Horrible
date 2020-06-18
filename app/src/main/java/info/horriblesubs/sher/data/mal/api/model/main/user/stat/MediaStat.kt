package info.horriblesubs.sher.data.mal.api.model.main.user.stat

import com.google.gson.annotations.SerializedName

open class MediaStat (
    @SerializedName("total_entries") var totalEntries: Int? = null,
    @SerializedName("mean_score") var meanScore: Float? = null,
    @SerializedName("completed") var completed: Int? = null,
    @SerializedName("dropped") var dropped: Int? = null,
    @SerializedName("on_hold") var onHold: Int? = null
)