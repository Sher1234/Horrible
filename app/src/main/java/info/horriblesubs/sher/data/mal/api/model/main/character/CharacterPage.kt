package info.horriblesubs.sher.data.mal.api.model.main.character

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.VoiceActor
import info.horriblesubs.sher.data.mal.api.model.common.page.BeingPage

class CharacterPage(
    @SerializedName("voice_actors") var voiceActors: ArrayList<VoiceActor>? = null,
    @SerializedName("animeography") var animes: ArrayList<Appearance>? = null,
    @SerializedName("mangaography") var mangas: ArrayList<Appearance>? = null,
    @SerializedName("nicknames") var nicknames: ArrayList<String>? = null,
    @SerializedName("name_kanji") var nameKanji: String? = null,
    memberFavorites: Int? = null,
    imageUrl: String? = null,
    about: String? = null,
    name: String? = null,
    url: String? = null,
    malId: Int? = null
): BeingPage(imageUrl = imageUrl, malId = malId, name = name, url = url, about = about, memberFavorites = memberFavorites)