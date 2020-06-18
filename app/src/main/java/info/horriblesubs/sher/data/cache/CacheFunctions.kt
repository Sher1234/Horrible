@file:JvmName("CacheFunctions")

package info.horriblesubs.sher.data.cache

import androidx.lifecycle.MutableLiveData
import info.horriblesubs.sher.data.RepositoryData
import info.horriblesubs.sher.data.RepositoryResult
import java.time.ZonedDateTime

fun <T> MutableLiveData<RepositoryResult<T>>.setLoading() {
    postValue(RepositoryResult.getLoading())
}

fun <T> MutableLiveData<RepositoryResult<T>>.setSuccess(value: T?) {
    postValue(RepositoryResult.getSuccess(value))
}

fun <T> MutableLiveData<RepositoryResult<T>>.setFailure(message: String) {
    postValue(RepositoryResult.getFailure(message))
}

fun MutableLiveData<ZonedDateTime>.set(value: ZonedDateTime? = null) {
    postValue(value)
}

inline fun <E, T: RepositoryData<E>> T?.isCacheInvalid(crossinline after: ZonedDateTime.() -> ZonedDateTime?): Boolean {
    val cVTime = this?.time?.zonedDateTimeISO?.after() ?: return true
    val nTime = ZonedDateTime.now() ?: return true
    return cVTime < nTime
}

val String?.zonedDateTimeISO: ZonedDateTime? get() {
    return ZonedDateTime.parse(this ?: return null)
}