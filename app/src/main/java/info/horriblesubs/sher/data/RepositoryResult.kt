package info.horriblesubs.sher.data

data class RepositoryResult<T>(
    val status: Int,
    val message: String,
    val value: T?,
) {
    companion object {
        const val FAILURE = -1
        const val LOADING = 0
        const val SUCCESS = 1

        fun <T> getLoading() = RepositoryResult<T>(LOADING, "", null,)
        fun <T> getSuccess(value: T?) = RepositoryResult<T>(SUCCESS, "", value)
        fun <T> getFailure(message: String) = RepositoryResult<T>(FAILURE, message, null)
    }
}