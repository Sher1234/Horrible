package info.horriblesubs.sher.data.mal.api.model.main.person

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.page.BeingPage
import info.horriblesubs.sher.data.mal.api.model.main.person.position.AnimeStaffPosition
import info.horriblesubs.sher.data.mal.api.model.main.person.position.PublishedManga
import info.horriblesubs.sher.data.mal.api.model.main.person.position.VoiceActingRole

class PersonPage(
    @SerializedName("anime_staff_positions") var animePositions: ArrayList<AnimeStaffPosition>? = null,
    @SerializedName("voice_acting_roles") var voiceActingRoles: ArrayList<VoiceActingRole>? = null,
    @SerializedName("published_manga") var publishedMangas: ArrayList<PublishedManga>? = null,
    @SerializedName("alternate_names") var alternateNames: ArrayList<String>? = null,
    @SerializedName("family_name") var familyName: String? = null,
    @SerializedName("website_url") var websiteUrl: String? = null,
    @SerializedName("given_name") var givenName: String? = null,
    @SerializedName("birthday") var birthday: String? = null,
    memberFavorites: Int? = null,
    imageUrl: String? = null,
    about: String? = null,
    name: String? = null,
    url: String? = null,
    malId: Int? = null
): BeingPage(imageUrl = imageUrl, malId = malId, name = name, url = url, about = about, memberFavorites = memberFavorites)