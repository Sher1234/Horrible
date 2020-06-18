package info.horriblesubs.sher.data.mal.api.model.main.anime

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.stats.Stats
import info.horriblesubs.sher.data.mal.api.model.common.stats.score.Score

class AnimeStatsPage(
    @SerializedName("plan_to_watch") var planToWatch: Int? = null,
    @SerializedName("watching") var watching: Int? = null,
    completed: Int? = null,
    dropped: Int? = null,
    onHold: Int? = null,
    score: Score? = null,
    total: Int? = null,
): Stats(completed = completed, dropped = dropped, onHold = onHold, score = score, total = total)