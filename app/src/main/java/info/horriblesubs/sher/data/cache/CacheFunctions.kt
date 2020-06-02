@file:JvmName("CacheFunctions")

package info.horriblesubs.sher.data.cache

import info.horriblesubs.sher.data.RepositoryData
import java.time.ZonedDateTime

inline fun <E, T: RepositoryData<E>> T?.isCacheInvalid(crossinline after: ZonedDateTime.() -> ZonedDateTime?): Boolean {
    val cVTime = this?.time?.zonedDateTimeISO?.after() ?: return true
    val nTime = ZonedDateTime.now() ?: return true
    return cVTime < nTime
}

val String?.zonedDateTimeISO: ZonedDateTime? get() {
    return ZonedDateTime.parse(this ?: return null)
}