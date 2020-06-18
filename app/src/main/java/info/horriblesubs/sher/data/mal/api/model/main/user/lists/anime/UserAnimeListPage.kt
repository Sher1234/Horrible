package info.horriblesubs.sher.data.mal.api.model.main.user.lists.anime

import com.google.gson.annotations.SerializedName

data class UserAnimeListPage(
    @SerializedName("anime") var animes: ArrayList<UserAnimeListItem>? = null
)