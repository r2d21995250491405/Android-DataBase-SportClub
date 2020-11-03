package com.example.sportclub.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.net.Uri;


import com.example.sportclub.data.ClubOlympusContract.MemberEntry;


public class OlympusContentProvider extends ContentProvider {
    OlympDBOpenHelper olympDBOpenHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(ClubOlympusContract.AUTHORITY, ClubOlympusContract.PATH_MEMBERS, 111);
        sUriMatcher.addURI(ClubOlympusContract.AUTHORITY, ClubOlympusContract.PATH_MEMBERS + "/#", 222);

    }

    @Override
    public boolean onCreate() {
        olympDBOpenHelper = new OlympDBOpenHelper(getContext());
        return true;
    }


    @Override
    // запрос данных из таблицы
    // content://com.example.sportclub.data/members/20
    // projection = {"firstname, lastname}
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = olympDBOpenHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case 111:
                cursor = db.query(MemberEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case 222:
                // selection = "_id=?"
                // selectionArgs = 20; поиск строки по id
                selection = MemberEntry.KEY_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MemberEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
//                Toast.makeText(getContext(), "Incorrect URI", Toast.LENGTH_SHORT).show();
                throw new IllegalArgumentException("Can't query incorrect URI " + uri);
        }
        // чтобы узнать об изменениях в базе
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public String getType(Uri uri) {


        int match = sUriMatcher.match(uri);
        switch (match) {
            case 111:
                return MemberEntry.CONTENT_MULTIPLE_ITEMS;
            case 222:
                return MemberEntry.CONTENT_SINGLE_ITEMS;

            default:
//                Toast.makeText(getContext(), "Incorrect URI", Toast.LENGTH_SHORT).show();
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String firsname = values.getAsString(MemberEntry.KEY_FIRST_NAME);
        if (firsname == null) {
            throw new IllegalArgumentException("You have to input Name URI " + uri);
        }
        String lastname = values.getAsString(MemberEntry.KEY_LAST_NAME);
        if (lastname == null) {
            throw new IllegalArgumentException("You have to input LastName URI " + uri);
        }
        Integer gender = values.getAsInteger(MemberEntry.KEY_GENDER);
        if (gender == null || !(gender == MemberEntry.GENDER_UNKNOWN || gender == MemberEntry.GENDER_MALE || gender == MemberEntry.GENDER_FEMALE)) {
            throw new IllegalArgumentException("You have to input GenderI " + uri);
        }
        String sport = values.getAsString(MemberEntry.KEY_SPORT);
        if (sport == null) {
            throw new IllegalArgumentException("You have to input Sport " + uri);
        }


        SQLiteDatabase db = olympDBOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        switch (match) {
            case 111:
                long id = db.insert(MemberEntry.TABLE_NAME, null, values);
                if (id == -1) {
//                    Log.e("insertMethod", "Failed insert" + uri);
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri, null);

                return ContentUris.withAppendedId(uri, id);
            default:
//                Toast.makeText(getContext(), "Incorrect URI", Toast.LENGTH_SHORT).show();
                throw new IllegalArgumentException("Can't query this URI " + uri);
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = olympDBOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int rowsDeleted;
        switch (match) {
            case 111:

                rowsDeleted = db.delete(MemberEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case 222:
                // selection = "_id=?"
                // selectionArgs = 20; поиск строки по id
                selection = MemberEntry.KEY_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(MemberEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
//                Toast.makeText(getContext(), "Incorrect URI", Toast.LENGTH_SHORT).show();
                throw new IllegalArgumentException("Can't delete this URI " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(MemberEntry.KEY_FIRST_NAME)) {
            String firstname = values.getAsString(MemberEntry.KEY_FIRST_NAME);
            if (firstname == null) {
                throw new IllegalArgumentException("You have to input Name URI " + uri);
            }
        }
        if (values.containsKey(MemberEntry.KEY_LAST_NAME)) {
            String lastname = values.getAsString(MemberEntry.KEY_LAST_NAME);
            if (lastname == null) {
                throw new IllegalArgumentException("You have to input Name URI " + uri);
            }
        }
        if (values.containsKey(MemberEntry.KEY_GENDER)) {
            Integer gender = values.getAsInteger(MemberEntry.KEY_GENDER);
            if (gender == null || !(gender == MemberEntry.GENDER_UNKNOWN || gender == MemberEntry.GENDER_MALE || gender == MemberEntry.GENDER_FEMALE)) {
                throw new IllegalArgumentException("You have to input GenderI " + uri);
            }
        }
        if (values.containsKey(MemberEntry.KEY_SPORT)) {
            String sport = values.getAsString(MemberEntry.KEY_SPORT);
            if (sport == null) {
                throw new IllegalArgumentException("You have to input Sport " + uri);
            }
        }


        SQLiteDatabase db = olympDBOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case 111:

                rowsUpdated = db.update(MemberEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case 222:
                // selection = "_id=?"
                // selectionArgs = 20; поиск строки по id
                selection = MemberEntry.KEY_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                rowsUpdated = db.update(MemberEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
//                Toast.makeText(getContext(), "Incorrect URI", Toast.LENGTH_SHORT).show();
                throw new IllegalArgumentException("Can't update incorrect URI " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
