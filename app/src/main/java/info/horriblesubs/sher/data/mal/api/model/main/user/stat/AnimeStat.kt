package info.horriblesubs.sher.data.mal.api.model.main.user.stat

import com.google.gson.annotations.SerializedName

class AnimeStat (
    @SerializedName("episodes_watched") var episodesWatched: Int? = null,
    @SerializedName("days_watched") var daysWatched: Float? = null,
    @SerializedName("plan_to_watch") var planToWatch: Int? = null,
    @SerializedName("rewatched") var reWatched: Int? = null,
    @SerializedName("watching") var watching: Int? = null,
    totalEntries: Int? = null,
    meanScore: Float? = null,
    completed: Int? = null,
    dropped: Int? = null,
    onHold: Int? = null
): MediaStat(totalEntries = totalEntries, meanScore = meanScore, completed = completed,
    dropped = dropped, onHold = onHold)