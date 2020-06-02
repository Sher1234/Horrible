package info.horriblesubs.sher.data.horrible

import info.horriblesubs.sher.data.database.AppDatabase
import info.horriblesubs.sher.data.database.model.BookmarkedShow

object LibraryRepository {
    private val library get() = AppDatabase.database.getLibrary()

    suspend fun insert(vararg items: BookmarkedShow) = library.insert(*items)

    fun getByIdLive(id: String) = library.getByIdLive(id)

    fun isPresent(id: String?) = if (id.isNullOrBlank()) false else
        library.countById(id).items == 1

    suspend fun getById(id: String?) = if (id.isNullOrBlank()) null else
        library.getById(id)

    suspend fun delete(id: String?) = if (id.isNullOrBlank()) Unit else
        library.delete(id)

    suspend fun getById(show: BookmarkedShow) = getById(show.uid)

    fun getByIdLive(show: BookmarkedShow) = getByIdLive(show.uid)

    suspend fun delete(item: BookmarkedShow) = delete(item.uid)

    fun isPresent(show: BookmarkedShow) = isPresent(show.uid)

    suspend fun delete(items: List<BookmarkedShow>) =
        library.delete(items)

    fun getAllNotLive() = library.getAllNotLive()

    fun getAll() = library.getAll()
}