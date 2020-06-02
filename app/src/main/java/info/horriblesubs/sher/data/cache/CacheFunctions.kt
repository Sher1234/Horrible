@file:JvmName("CacheFunctions")

package info.horriblesubs.sher.data.cache

import info.horriblesubs.sher.data.RepositoryData
import java.time.ZonedDateTime

inline fun <E, T: RepositoryData<E>> T?.isCacheInvalid(after: ZonedDateTime.() -> ZonedDateTime?): Boolean {
    val cVTime2 = this?.time?.zonedDateTimeISO?.after() ?: return true
    val nTime = ZonedDateTime.now() ?: return true
    return cVTime2 < nTime
}

val String?.zonedDateTimeISO: ZonedDateTime? get() {
    return ZonedDateTime.parse(this ?: return null)
}