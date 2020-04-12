package info.horriblesubs.sher.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(c: Context): SQLiteOpenHelper(c, "FavDB", null, 4) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE " + Horrible.Favourites + " (" +
                    Horrible.ShowID + " TEXT PRIMARY KEY, " +
                    Horrible.ID + " TEXT UNIQUE, " +
                    Horrible.Image + " TEXT, " +
                    Horrible.Title + " TEXT, " +
                    Horrible.Link + " TEXT)"
        )
        db?.execSQL(
            "CREATE TABLE " + Horrible.Notify + " (" +
                    Horrible.ID + " TEXT PRIMARY KEY, " +
                    Horrible.Release + " TEXT, " +
                    Horrible.Title + " TEXT, " +
                    Horrible.Link + " TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + Horrible.Favourites)
        db.execSQL("DROP TABLE IF EXISTS " + Horrible.Notify)
        onCreate(db)
    }

    object Horrible {
        const val Favourites = "Favourites_Horrible"
        const val Notify = "Notifications_Horrible"
        const val Release = "Number"
        const val ShowID = "ShowID"
        const val Image = "Image"
        const val Title = "Title"
        const val Link = "Link"
        const val ID = "ID"
    }
}