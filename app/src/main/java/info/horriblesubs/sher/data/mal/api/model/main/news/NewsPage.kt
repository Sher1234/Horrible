package info.horriblesubs.sher.data.mal.api.model.main.news

import com.google.gson.annotations.SerializedName

data class NewsPage(
    @SerializedName("articles") var articles: ArrayList<Article>? = null
)