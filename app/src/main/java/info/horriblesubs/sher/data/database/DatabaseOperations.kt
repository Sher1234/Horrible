package info.horriblesubs.sher.data.database

import androidx.annotation.WorkerThread
import info.horriblesubs.sher.data.database.model.BookmarkedShow
import info.horriblesubs.sher.data.database.model.NotificationItem
import info.horriblesubs.sher.data.subsplease.LibraryRepository
import info.horriblesubs.sher.data.subsplease.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> operateOnDB(@WorkerThread run: suspend () -> T, after: T.() -> Unit = {}) {
    GlobalScope.launch {
        withContext(Dispatchers.IO) {
            run()
        }.after()
    }
}

fun onBookmarkChange(show: BookmarkedShow) {
    GlobalScope.launch {
        withContext(Dispatchers.IO) {
            if (LibraryRepository.isPresent(show))
                LibraryRepository.delete(show)
            else
                LibraryRepository.insert(show)
        }
    }
}

fun inLibrary(link: String, @WorkerThread run: Boolean.() -> Unit = {}) {
    LibraryRepository.isPresent(link).run()
}

fun isNotified(item: NotificationItem, run: Boolean.() -> Unit = {}) {
    GlobalScope.launch(Dispatchers.Main) {
        withContext(Dispatchers.IO) {
            if (NotificationRepository.isPresent(item.id))
                true else {
                NotificationRepository.insert(item)
                false
            }
        }.run()
    }
}