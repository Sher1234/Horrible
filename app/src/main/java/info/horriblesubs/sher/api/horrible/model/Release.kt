package info.horriblesubs.sher.api.horrible.model

import java.io.Serializable

class Release (val downloads: List<Map<String, String>>, val quality: List<Boolean>?, val release: String?, var batch: Boolean = false): Serializable