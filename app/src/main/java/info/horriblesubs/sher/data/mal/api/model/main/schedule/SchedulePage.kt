package info.horriblesubs.sher.data.mal.api.model.main.schedule

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.AnimeItem

data class SchedulePage (
    @SerializedName("wednesday") var wednesday: ArrayList<AnimeItem>? = null,
    @SerializedName("saturday") var saturday: ArrayList<AnimeItem>? = null,
    @SerializedName("thursday") var thursday: ArrayList<AnimeItem>? = null,
    @SerializedName("unknown") var unknown: ArrayList<AnimeItem>? = null,
    @SerializedName("tuesday") var tuesday: ArrayList<AnimeItem>? = null,
    @SerializedName("friday") var friday: ArrayList<AnimeItem>? = null,
    @SerializedName("sunday") var sunday: ArrayList<AnimeItem>? = null,
    @SerializedName("monday") var monday: ArrayList<AnimeItem>? = null,
    @SerializedName("other") var others: ArrayList<AnimeItem>? = null
)