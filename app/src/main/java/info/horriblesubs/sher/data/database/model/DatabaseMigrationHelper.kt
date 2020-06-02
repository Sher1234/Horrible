package info.horriblesubs.sher.data.database.model

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import info.horriblesubs.sher.App
import info.horriblesubs.sher.data.database.AppDatabase
import info.horriblesubs.sher.data.database.operateOnDB
import java.io.File

object DatabaseMigrationHelper {
    private val app = App.get()
    private const val oldDbName = "FavDB"
    private const val tag = "DatabaseMigration"
    private val dbFile = app.getDatabasePath(
        oldDbName)

    const val FavouritesTable = "Favourites_Horrible"
    const val NotifyTable = "Notifications_Horrible"
    const val Release = "Number"
    const val ShowID = "ShowID"
    const val Image = "Image"
    const val Title = "Title"
    const val Link = "Link"
    const val ID = "ID"

    private fun getDatabase(path: String) = try {
        SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    private val isDbExists: Boolean get() = dbFile.exists()

    fun onMigrate() {
        if (isDbExists) {
            Log.d(tag, "Database migration available.")
            try {
                val access =
                    DataAccess(
                        dbFile.path)
                operateOnDB({
                    access.getNotifications()?.let {
                        AppDatabase.database.getHorribleNotifications().insertAll(it)
                    }
                    access.getBookmarks()?.let {
                        AppDatabase.database.getLibrary().insertAll(it)
                    }
                }) {
                    val file = File(dbFile.path + "-journal")
                    Log.d(tag, if (dbFile.delete()) "Database migrated to v90" else "Unable to migrate database to v90")
                    Log.d(tag, if (file.exists()) if (file.delete()) "Journal Deleted" else "Can't delete journal" else "File does not exist")
                }
            } catch (e: Exception) {
                Log.w(tag, "Unable to migrate database.", e)
            }
        } else Log.d(tag, "Database migration not available.")
    }

    class DataAccess(private val path: String) {

        fun getBookmarks(): List<BookmarkedShow>? {
            val projection = arrayOf(ShowID,
                Title,
                Image,
                Link,
                ID)
            val shows = arrayListOf<BookmarkedShow>()
            getDatabase(
                path)?.use { it ->
                try {
                    it.query(FavouritesTable, projection, null, null, null, null, null).use { cursor ->
                        val title = cursor.getColumnIndex(Title)
                        val image = cursor.getColumnIndex(Image)
                        val sid = cursor.getColumnIndex(ShowID)
                        val link = cursor.getColumnIndex(Link)
                        val id = cursor.getColumnIndex(ID)
                        cursor.moveToFirst()
                        while (!cursor.isAfterLast) {
                            shows.add(BookmarkedShow(
                                body = "Not yet initialized",
                                title = cursor.getString(title),
                                image = cursor.getString(image),
                                link = cursor.getString(link),
                                sid = cursor.getString(sid),
                                id = cursor.getString(id)
                            ))
                            cursor.moveToNext()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    it.close()
                    return if (shows.isEmpty()) null else shows
                }
            }
            return if (shows.isEmpty()) null else shows
        }

        fun getNotifications(): List<NotificationItem>? {
            val notifications = arrayListOf<NotificationItem>()
            val projection = arrayOf(ID,
                Title,
                Release,
                Link)
            getDatabase(
                path)?.use { it ->
                try {
                    it.query(NotifyTable, projection, null, null, null, null, null).use { cursor ->
                        val release = cursor.getColumnIndex(Release)
                        val title = cursor.getColumnIndex(Title)
                        val link = cursor.getColumnIndex(Link)
                        val id = cursor.getColumnIndex(ID)
                        cursor.moveToFirst()
                        while (!cursor.isAfterLast) {
                            notifications.add(NotificationItem(
                                release = cursor.getString(release),
                                title = cursor.getString(title),
                                link = cursor.getString(link),
                                id = cursor.getString(id)
                            ))
                            cursor.moveToNext()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    it.close()
                    return if (notifications.isEmpty()) null else notifications
                }
            }
            return if (notifications.isEmpty()) null else notifications
        }
    }
}