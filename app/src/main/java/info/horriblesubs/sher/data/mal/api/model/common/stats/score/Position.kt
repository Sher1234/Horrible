package info.horriblesubs.sher.data.mal.api.model.common.stats.score

import com.google.gson.annotations.SerializedName


data class Position (
    @SerializedName("percentage") var percentage: Double? = null,
    @SerializedName("votes") var votes: Int = 0
)
