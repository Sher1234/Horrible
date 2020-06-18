package info.horriblesubs.sher.data.mal.api.model.main.season

import com.google.gson.annotations.SerializedName

data class SeasonalAnimePage (
    @SerializedName("anime") var animes: ArrayList<SeasonItem>? = null,
    @SerializedName("season_name") var seasonName: String? = null,
    @SerializedName("season_year") var seasonYear: Int? = null,
)