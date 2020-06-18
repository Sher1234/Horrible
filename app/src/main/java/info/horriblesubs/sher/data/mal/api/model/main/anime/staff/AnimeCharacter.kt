package info.horriblesubs.sher.data.mal.api.model.main.anime.staff

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.VoiceActor
import info.horriblesubs.sher.data.mal.api.model.common.media_character.MediaCharacter

class AnimeCharacter(
    @SerializedName("voice_actors") var voiceActors: ArrayList<VoiceActor>? = null,
    imageUrl: String? = null,
    name: String? = null,
    role: String? = null,
    url: String? = null,
    malId: Int? = null,
): MediaCharacter(imageUrl = imageUrl, malId = malId, name = name, url = url, role = role)