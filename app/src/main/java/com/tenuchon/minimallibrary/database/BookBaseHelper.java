package com.tenuchon.minimallibrary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.tenuchon.minimallibrary.database.BookDBScheme.*;

public class BookBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "bookBase.db";

    public BookBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LibraryTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                LibraryTable.Cols.UUID + ", " +
                LibraryTable.Cols.DATE + ", " +
                LibraryTable.Cols.TITLE + ", " +
                LibraryTable.Cols.AUTHOR + ", " +
                LibraryTable.Cols.DESCRIPTION + ", " +
                LibraryTable.Cols.FAVORITE + ", " +
                LibraryTable.Cols.WISH + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
