package com.example.simple_notes.simplenotes;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NotesProvider extends ContentProvider{

    //URI Constants
    private static final String AUTHORITY = "com.example.simple_notes.notesprovider";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    //Constants to ID the requested operation
    private static final int NOTES = 1;
    private static final int NOTE_ID = 2;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final String CONTENT_ITEM_TYPE = "Note";

    static {
        matcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        matcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTE_ID);
    }

    SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        //Get/Create SQL DB
        DBOpenHelpher helper = new DBOpenHelpher(getContext());
         database = helper.getWritableDatabase();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if(matcher.match(uri) == NOTE_ID){
            selection = DBOpenHelpher.NOTE_ID + "=" + uri.getLastPathSegment();
        }

        return database.query(DBOpenHelpher.TABLE_NOTES, DBOpenHelpher.ALL_COLUMNS, selection,
                null, null, null, DBOpenHelpher.NOTE_CREATED + " DESC");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert(DBOpenHelpher.TABLE_NOTES, null, values);

        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete(DBOpenHelpher.TABLE_NOTES, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update(DBOpenHelpher.TABLE_NOTES, values, selection, selectionArgs);
    }
}
