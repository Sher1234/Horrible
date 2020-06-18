package info.horriblesubs.sher.data.mal.api.model.main.top

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.main.top.base.TopListingMedia

class TopMangaItem(
    @SerializedName("volumes") var volumes: Int? = null,
    startDate: String? = null,
    imageUrl: String? = null,
    endDate: String? = null,
    title: String? = null,
    members: Int? = null,
    score: Float? = null,
    type: String? = null,
    malId: Int? = null,
    url: String? = null,
    rank: Int? = null
): TopListingMedia(startDate = startDate, endDate = endDate, members = members, score = score,
    type = type, imageUrl = imageUrl, title = title, malId = malId, url = url, rank = rank)