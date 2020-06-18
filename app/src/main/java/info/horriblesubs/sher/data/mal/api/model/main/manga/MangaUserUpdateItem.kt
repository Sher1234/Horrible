package info.horriblesubs.sher.data.mal.api.model.main.manga

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.updates.UserUpdate

class MangaUserUpdateItem (
    @SerializedName("chapters_total") var chaptersTotal: Int? = null,
    @SerializedName("chapters_read") var chaptersRead: Int? = null,
    @SerializedName("volumes_total") var volumesTotal: Int? = null,
    @SerializedName("volumes_read") var volumesRead: Int? = null,
    imageUrl: String? = null,
    username: String? = null,
    status: String? = null,
    scores: Int? = null,
    date: String? = null,
    url: String? = null
): UserUpdate(imageUrl, username, status, scores, date, url)