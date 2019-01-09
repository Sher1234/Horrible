package info.horriblesubs.sher.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

class Database extends SQLiteOpenHelper {

    private static final String DB = "Anime_RLS";
    private static final int VERSION = 3;

    Database(@Nullable Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Latest
        db.execSQL("CREATE TABLE " + Horrible.H_Latest + " (" +
                Horrible.ID + " TEXT PRIMARY KEY, " +
                Horrible.L_Release + " TEXT, " +
                Horrible.Title + " TEXT, " +
                Horrible.Link + " TEXT, " +
                Horrible.L_SD + " INTEGER DEFAULT 0, " +
                Horrible.L_HD + " INTEGER DEFAULT 0, " +
                Horrible.L_FHD + " INTEGER DEFAULT 0)"
        );
        //Favourites
        db.execSQL("CREATE TABLE " + Horrible.H_Favourites + " (" +
                Horrible.F_ShowID + " TEXT PRIMARY KEY, " +
                Horrible.ID + " TEXT UNIQUE, " +
                Horrible.F_Image + " TEXT, " +
                Horrible.Title + " TEXT, " +
                Horrible.Link + " TEXT)"
        );
        //Current Shows
        db.execSQL("CREATE TABLE " + Horrible.H_Current + " (" +
                Horrible.ID + " TEXT PRIMARY KEY, " +
                Horrible.Title + " TEXT, " +
                Horrible.Link + " TEXT)"
        );
        //All Shows
        db.execSQL("CREATE TABLE " + Horrible.H_All + " (" +
                Horrible.ID + " TEXT PRIMARY KEY, " +
                Horrible.Title + " TEXT, " +
                Horrible.Link + " TEXT)"
        );
        //Schedule
        db.execSQL("CREATE TABLE " + Horrible.H_Schedule + " (" +
                Horrible.ID + " TEXT PRIMARY KEY, " +
                Horrible.Title + " TEXT, " +
                Horrible.S_Time + " TEXT, " +
                Horrible.Link + " TEXT, " +
                Horrible.S_Scheduled + " INTEGER DEFAULT 0)"
        );
        //Notifications
        db.execSQL("CREATE TABLE " + Horrible.H_Notify + " (" +
                Horrible.ID + " TEXT PRIMARY KEY, " +
                Horrible.L_Release + " TEXT, " +
                Horrible.Title + " TEXT, " +
                Horrible.Link + " TEXT)"
        );
        //UpdatedOn
        db.execSQL("CREATE TABLE " + Horrible.H_Updates + " (Item TEXT PRIMARY KEY, Time TEXT)");
        db.execSQL("INSERT INTO " + Horrible.H_Updates + " (Item) VALUES ('Releases'), ('Shows'), ('Schedule')");

        //Horrible Saved Table
//        db.execSQL("CREATE TABLE LatestN (" +
//                "ID TEXT PRIMARY KEY, " +
//                "Name TEXT, " +
//                "Size TEXT, " +
//                "Time TEXT, " +
//                "Magnet TEXT, " +
//                "Torrent TEXT, " +
//                "Category TEXT, " +
//                "Trusted INTEGER DEFAULT 0, " +
//                "Seeders INTEGER DEFAULT 0, " +
//                "Leechers INTEGER DEFAULT 0," +
//                "Completed INTEGER DEFAULT 0)");
//        db.execSQL("CREATE TABLE FavouritesN (Submitter TEXT PRIMARY KEY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Horrible.H_Favourites);
        db.execSQL("DROP TABLE IF EXISTS " + Horrible.H_Schedule);
        db.execSQL("DROP TABLE IF EXISTS " + Horrible.H_Notify);
        db.execSQL("DROP TABLE IF EXISTS " + Horrible.H_Updates);
        db.execSQL("DROP TABLE IF EXISTS " + Horrible.H_Current);
        db.execSQL("DROP TABLE IF EXISTS " + Horrible.H_Latest);
        db.execSQL("DROP TABLE IF EXISTS " + Horrible.H_All);
//        db.execSQL("DROP TABLE IF EXISTS FavouritesN");
//        db.execSQL("DROP TABLE IF EXISTS LatestN");
        onCreate(db);
    }

    public interface Horrible {
        String H_Favourites = "Favourites_Horrible";
        String H_Notify = "Notifications_Horrible";
        String H_Updates = "Refreshed_Horrible";
        String H_Schedule = "Schedule_Horrible";
        String H_Current = "Current_Horrible";
        String H_Latest = "Latest_Horrible";
        String H_All = "All_Horrible";

        String S_Scheduled = "Scheduled";
        String S_Time = "Time";

        String L_Release = "Number";
        String L_FHD = "FHD";
        String L_HD = "HD";
        String L_SD = "SD";

        String F_ShowID = "ShowID";
        String F_Image = "Image";

        String Title = "Title";
        String Link = "Link";
        String ID = "ID";
    }
}