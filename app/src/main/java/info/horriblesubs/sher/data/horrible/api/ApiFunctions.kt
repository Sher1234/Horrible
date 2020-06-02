@file:JvmName("ApiFunctions")

package info.horriblesubs.sher.data.horrible.api

import info.horriblesubs.sher.data.horrible.api.model.ItemBase
import info.horriblesubs.sher.data.horrible.api.model.ItemList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


suspend fun List<ItemList>?.current() = withContext(Dispatchers.IO) {
    this@current?.filter { it.ongoing } ?: emptyList()
}

val Boolean.resetShowHorribleAPI: String get() = if (this) ".reset" else ""

val Boolean.resetHorribleAPI: String get() = if (this) "reset" else "-"

val ItemBase?.imageUrl: String? get() {
    val image = this?.image
    return if(image.isNullOrBlank() || image.contains("placeholder", true))
        null else image
}