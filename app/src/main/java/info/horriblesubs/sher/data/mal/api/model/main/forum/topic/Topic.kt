package info.horriblesubs.sher.data.mal.api.model.main.forum.topic

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.Base

class Topic(
    @SerializedName("date_posted") var datePosted: String? = null,
    @SerializedName("author_name") var authorName: String? = null,
    @SerializedName("last_post") var lastPost: LastPost? = null,
    @SerializedName("author_url") var authorUrl: String? = null,
    @SerializedName("topic_id") var topicId: Int? = null,
    @SerializedName("replies") var replies: Int? = null,
    @SerializedName("title") var title: String? = null,
    url: String? = null
): Base(null, url)