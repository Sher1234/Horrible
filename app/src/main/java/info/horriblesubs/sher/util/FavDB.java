package info.horriblesubs.sher.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import info.horriblesubs.sher.Strings;

class FavDB extends SQLiteOpenHelper implements Strings {

    private static final int version = 2;

    FavDB(Context context) {
        super(context, FavDB, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + FavTable + " (" +
                FavID + " INTEGER PRIMARY KEY, " +
                FavShowID + " TEXT, " +
                FavTitle + " TEXT, " +
                FavImage + " TEXT, " +
                FavLink + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavTable);
        onCreate(sqLiteDatabase);
    }
}