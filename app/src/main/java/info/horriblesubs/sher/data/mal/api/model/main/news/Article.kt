package info.horriblesubs.sher.data.mal.api.model.main.news

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.Base

class Article (
    @SerializedName("author_name") var authorName: String? = null,
    @SerializedName("author_url") var authorUrl: String? = null,
    @SerializedName("forum_url") var forumUrl: String? = null,
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("comments") var comments: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("intro") var intro: String? = null,
    @SerializedName("date") var date: String? = null,
    url: String? = null
): Base(null, url)