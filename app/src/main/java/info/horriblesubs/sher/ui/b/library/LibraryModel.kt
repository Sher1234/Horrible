package info.horriblesubs.sher.ui.b.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.horriblesubs.sher.data.database.model.BookmarkedShow
import info.horriblesubs.sher.data.subsplease.LibraryRepository

class LibraryModel: ViewModel() {
    val resource: LiveData<List<BookmarkedShow>> = LibraryRepository.getAll()
    val isDeleteEnabled = MutableLiveData<Boolean>().apply { value = false }
    var toggle: Boolean
        set(value) = isDeleteEnabled.setValue(value)
        get() = isDeleteEnabled.value ?: false
}