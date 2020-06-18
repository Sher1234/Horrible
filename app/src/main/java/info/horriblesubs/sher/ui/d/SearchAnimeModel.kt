package info.horriblesubs.sher.ui.d

import androidx.lifecycle.*
import info.horriblesubs.sher.data.mal.MalSearchRepository
import info.horriblesubs.sher.data.mal.api.model.common.search.AdvancedSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SearchAnimeModel: ViewModel() {

    class SearchObject<T: AdvancedSearch> (var query: String, var filter: T) {
        val map get() = filter.get()
    }

    private val liveSearchObject = MutableLiveData<SearchObject<AdvancedSearch.Anime>>()
    private val repository = MalSearchRepository.Anime()

    var searchObject: SearchObject<AdvancedSearch.Anime>?
        set(value) = liveSearchObject.postValue(value)
        get() = liveSearchObject.value

    @FlowPreview
    val repoResult = liveSearchObject.switchMap {
        liveData { emitSource(repository.getSearch(it.query, it.map).asLiveData(Dispatchers.Main)) }
    }

    fun onSearch(query: String) {
        liveSearchObject.postValue(SearchObject(query, AdvancedSearch.Anime()))
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + 6 + lastVisibleItemPosition < totalItemCount) return
        liveSearchObject.value?.let { viewModelScope.launch { repository.requestMore(it.query, it.map) } }
    }
}