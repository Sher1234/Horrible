package info.horriblesubs.sher.data.mal.api.model.main.forum.topic

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.Base

class LastPost(
    @SerializedName("date_posted") var datePosted: String? = null,
    @SerializedName("author_name") var authorName: String? = null,
    @SerializedName("author_url") var authorUrl: String? = null,
    url: String? = null
): Base(malId = null, url = url)