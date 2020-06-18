package info.horriblesubs.sher.data.mal.api.model.main.user.stat

import com.google.gson.annotations.SerializedName

class MangaStat (
    @SerializedName("chapters_read") var chaptersRead: Int? = null,
    @SerializedName("volumes_read") var volumesRead: Int? = null,
    @SerializedName("plan_to_read") var planToRead: Int? = null,
    @SerializedName("days_read") var daysRead: Float? = null,
    @SerializedName("reading") var reading: Int? = null,
    @SerializedName("reread") var reRead: Int? = null,
    totalEntries: Int? = null,
    meanScore: Float? = null,
    completed: Int? = null,
    dropped: Int? = null,
    onHold: Int? = null
): MediaStat(totalEntries = totalEntries, meanScore = meanScore, completed = completed,
    dropped = dropped, onHold = onHold)