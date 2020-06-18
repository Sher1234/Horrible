package info.horriblesubs.sher.data.mal.api.model.main.manga

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.stats.Stats
import info.horriblesubs.sher.data.mal.api.model.common.stats.score.Score

class MangaStatsPage(
    @SerializedName("plan_to_read") var planToRead: Int? = null,
    @SerializedName("reading") var reading: Int? = null,
    completed: Int? = null,
    dropped: Int? = null,
    onHold: Int? = null,
    score: Score? = null,
    total: Int? = null,
): Stats(completed = completed, dropped = dropped, onHold = onHold, score = score, total = total)