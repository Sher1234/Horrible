package info.horriblesubs.sher.data.mal.api.model.main.person

import info.horriblesubs.sher.data.mal.api.model.common.search.BeingSearchItem

class PersonSearchItem(
    alternativeNames: ArrayList<String>? = null,
    imageUrl: String? = null,
    name: String? = null,
    url: String? = null,
    malId: Int? = null,
): BeingSearchItem(imageUrl = imageUrl, malId = malId, name = name, url = url, alternativeNames = alternativeNames)