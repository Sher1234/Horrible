package info.horriblesubs.sher.data.mal.api.model.main.anime

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.main.reviews.Scores

class AnimeScore(
    @SerializedName("animation") var animation: Int? = null,
    @SerializedName("sound") var sound: Int? = null,
    character: Int?,
    enjoyment: Int?,
    overall: Int?,
    story: Int?,
): Scores(overall = overall, story = story, character = character, enjoyment = enjoyment)