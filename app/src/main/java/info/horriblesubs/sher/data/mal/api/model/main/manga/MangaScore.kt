package info.horriblesubs.sher.data.mal.api.model.main.manga

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.main.reviews.Scores

class MangaScore(
    @SerializedName("art") var art: Int? = null,
    character: Int?,
    enjoyment: Int?,
    overall: Int?,
    story: Int?,
): Scores(overall = overall, story = story, character = character, enjoyment = enjoyment)