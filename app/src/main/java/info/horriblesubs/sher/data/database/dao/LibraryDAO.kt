package info.horriblesubs.sher.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import info.horriblesubs.sher.data.database.model.BookmarkedShow
import info.horriblesubs.sher.data.database.model.CountItem

@Dao
interface LibraryDAO {
    @Query("SELECT COUNT(*) AS items FROM Bookmarks WHERE sid = :sid OR url = :sid")
    fun countById(sid: String): CountItem

    @Query("SELECT * FROM Bookmarks WHERE sid = :sid OR url = :sid LIMIT 1")
    suspend fun getById(sid: String): BookmarkedShow?

    @Query("SELECT * FROM Bookmarks WHERE sid = :sid OR url = :sid")
    fun getByIdLive(sid: String): LiveData<List<BookmarkedShow>>

    @Delete
    suspend fun delete(items: List<BookmarkedShow>)

    @Query ("DELETE FROM Bookmarks WHERE sid = :sid OR url = :sid")
    suspend fun delete(sid: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<BookmarkedShow>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg items: BookmarkedShow)

    @Query("SELECT * FROM Bookmarks")
    fun getAll(): LiveData<List<BookmarkedShow>>

    @Query("SELECT * FROM Bookmarks")
    fun getAllNotLive(): List<BookmarkedShow>
}