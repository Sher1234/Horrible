package info.horriblesubs.sher.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import info.horriblesubs.sher.App
import info.horriblesubs.sher.data.database.dao.LibraryDAO
import info.horriblesubs.sher.data.database.dao.NotificationsDAO
import info.horriblesubs.sher.data.database.model.BookmarkedShow
import info.horriblesubs.sher.data.database.model.NotificationItem

@Database(
    entities = [NotificationItem::class, BookmarkedShow::class],
    exportSchema = false,
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getHorribleNotifications(): NotificationsDAO
    abstract fun getLibrary(): LibraryDAO
    companion object {
        val database: AppDatabase = Room
            .databaseBuilder(App.get(), AppDatabase::class.java, "store.db")
            .fallbackToDestructiveMigrationOnDowngrade()
            .allowMainThreadQueries()
            .build()
    }
}