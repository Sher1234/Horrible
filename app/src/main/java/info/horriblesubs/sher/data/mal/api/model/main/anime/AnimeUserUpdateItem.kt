package info.horriblesubs.sher.data.mal.api.model.main.anime

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.updates.UserUpdate

class AnimeUserUpdateItem (
    @SerializedName("episodes_total") var episodesTotal: Int? = null,
    @SerializedName("episodes_seen") var episodesSeen: Int? = null,
    imageUrl: String? = null,
    username: String? = null,
    status: String? = null,
    scores: Int? = null,
    date: String? = null,
    url: String? = null,
): UserUpdate(imageUrl = imageUrl, username = username, status = status,
    scores = scores, date = date, url = url)