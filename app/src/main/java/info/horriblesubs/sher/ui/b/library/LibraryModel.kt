package info.horriblesubs.sher.ui.b.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.horriblesubs.sher.data.database.model.BookmarkedShow
import info.horriblesubs.sher.data.horrible.LibraryRepository

class LibraryModel: ViewModel() {
    val isDeleteEnabled = MutableLiveData<Boolean>().apply {
        value = false
    }
    val resource: LiveData<List<BookmarkedShow>> = LibraryRepository.getAll()
}