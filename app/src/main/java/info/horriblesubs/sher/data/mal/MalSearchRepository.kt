package info.horriblesubs.sher.data.mal

import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.mal.api.MalApi
import info.horriblesubs.sher.data.mal.api.model.common.search.SearchItem
import info.horriblesubs.sher.data.mal.api.model.main.anime.AnimeSearchItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@ExperimentalCoroutinesApi
sealed class MalSearchRepository {

    companion object { const val START_INDEX = 1 }

    abstract class Search<T: SearchItem> {
        protected val searchResults = ConflatedBroadcastChannel<RepositoryResult<List<T>>>()
        protected var lastRequestedPage: Int = START_INDEX
        protected val inMemoryCache = mutableListOf<T>()
        protected var lastAvailablePage = START_INDEX
        protected var isRequestInProgress = false

        @FlowPreview
        abstract suspend fun getSearch(query: String, filter: Map<String, String>): Flow<RepositoryResult<List<T>>>
        abstract suspend fun requestAndSaveData(query: String, filter: Map<String, String>): Boolean
        abstract suspend fun requestMore(query: String, filter: Map<String, String>)
        abstract suspend fun retry(query: String, filter: Map<String, String>)
    }

    class Anime: Search<AnimeSearchItem>() {
        @FlowPreview
        override suspend fun getSearch(query: String, filter: Map<String, String>): Flow<RepositoryResult<List<AnimeSearchItem>>> {
            if (query.isNotBlank()) {
                lastRequestedPage = START_INDEX
                inMemoryCache.clear()
                requestAndSaveData(query, filter)
            }
            return searchResults.asFlow()
        }

        override suspend fun requestAndSaveData(query: String, filter: Map<String, String>): Boolean {
            isRequestInProgress = true
            searchResults.offer(RepositoryResult.getLoading())
            return try {
                val response = MalApi.Search.service.getSearchAnime(query, lastRequestedPage, filter)
                lastAvailablePage = response?.lastPage ?: lastAvailablePage
                val repos = response?.results ?: emptyList()
                inMemoryCache.addAll(repos)
                searchResults.offer(RepositoryResult.getSuccess(inMemoryCache))
                isRequestInProgress = false
                true
            } catch (e: Exception) {
                searchResults.offer(RepositoryResult.getFailure(e.message?:"Error"))
                isRequestInProgress = false
                e.printStackTrace()
                false
            }
        }

        override suspend fun requestMore(query: String, filter: Map<String, String>) {
            if (isRequestInProgress || lastRequestedPage > lastAvailablePage || query.isBlank()) return
            val successful = requestAndSaveData(query, filter)
            if (successful) lastRequestedPage++
        }

        override suspend fun retry(query: String, filter: Map<String, String>) {
            if (isRequestInProgress || query.isBlank()) return
            requestAndSaveData(query, filter)
        }
    }
}