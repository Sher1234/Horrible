package info.horriblesubs.sher.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

class Database extends SQLiteOpenHelper {

    private static final String DB = "FavDB";
    private static final int VERSION = 4;

    Database(@Nullable Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Favourites
        db.execSQL("CREATE TABLE " + Horrible.Favourites + " (" +
                Horrible.ShowID + " TEXT PRIMARY KEY, " +
                Horrible.ID + " TEXT UNIQUE, " +
                Horrible.Image + " TEXT, " +
                Horrible.Title + " TEXT, " +
                Horrible.Link + " TEXT)"
        );
        //Notifications
        db.execSQL("CREATE TABLE " + Horrible.Notify + " (" +
                Horrible.ID + " TEXT PRIMARY KEY, " +
                Horrible.Release + " TEXT, " +
                Horrible.Title + " TEXT, " +
                Horrible.Link + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Horrible.Favourites);
        db.execSQL("DROP TABLE IF EXISTS " + Horrible.Notify);
        onCreate(db);
    }

    public interface Horrible {
        String Favourites = "Favourites_Horrible";
        String Notify = "Notifications_Horrible";
        String Release = "Number";
        String ShowID = "ShowID";
        String Image = "Image";
        String Title = "Title";
        String Link = "Link";
        String ID = "ID";
    }
}