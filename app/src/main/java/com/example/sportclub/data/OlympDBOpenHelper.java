package com.example.sportclub.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sportclub.data.ClubOlympusContract.MemberEntry;

import androidx.annotation.Nullable;

public class OlympDBOpenHelper extends SQLiteOpenHelper {
    public OlympDBOpenHelper(Context context) {
        super(context, ClubOlympusContract.DATABASE_NAME, null, ClubOlympusContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + MemberEntry.TABLE_NAME
                + "(" + MemberEntry.KEY_ID + " INTEGER PRIMARY KEY,"
                + MemberEntry.KEY_FIRST_NAME + " TEXT,"
                + MemberEntry.KEY_LAST_NAME + " TEXT,"
                + MemberEntry.KEY_GENDER + " INTEGER NOT NULL,"
                + MemberEntry.KEY_SPORT + " TEXT)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MemberEntry.TABLE_NAME);
        onCreate(db);
    }
}
