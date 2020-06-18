package info.horriblesubs.sher.data.mal.api.model.main.user.lists.manga

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.main.magazine.Magazine

data class UserMangaListItem(
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("score") var score: Double? = null,
    @SerializedName("mal_id") var malId: Int? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("url") var url: String? = null,

    @SerializedName("total_chapters") var totalChapters: Int? = null,
    @SerializedName("reading_status") var readingStatus: Int? = null,
    @SerializedName("read_chapters") var readChapters: Int? = null,
    @SerializedName("total_volumes") var totalVolumes: Int? = null,
    @SerializedName("read_volumes") var readVolumes: Int? = null,

    @SerializedName("publishing_status") var publishingStatus: Int? = null,
    @SerializedName("is_rereading") var isRereading: Boolean? = null,

    //TODO: This will become an error in the future, well. Ill be given a user tag that i can use
    @SerializedName("tags") var tags: ArrayList<String>? = null,

    @SerializedName("read_start_date") var readStartDate: String? = null,
    @SerializedName("read_end_date") var readEndDate: String? = null,
    @SerializedName("start_date") var startDate: String? = null,
    @SerializedName("end_date") var endDate: String? = null,

    //TODO: Not sure of this
    @SerializedName("days") var days: Int? = null,
    //TODO: Also unsure
    @SerializedName("retail") var retail: String? = null,

    @SerializedName("priority") var priority: String? = null,
    @SerializedName("added_to_list") var addedToList: Boolean? = null,

    //TODO: This will become an error in the future, well. Ill be given a user tag that i can use
    @SerializedName("magazines") var magazines: ArrayList<Magazine>? = null
)
