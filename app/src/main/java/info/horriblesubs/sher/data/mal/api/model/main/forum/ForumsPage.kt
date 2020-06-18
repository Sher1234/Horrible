package info.horriblesubs.sher.data.mal.api.model.main.forum

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.main.forum.topic.Topic

data class ForumsPage(
    @SerializedName("topics") var topics: ArrayList<Topic>? = null
)