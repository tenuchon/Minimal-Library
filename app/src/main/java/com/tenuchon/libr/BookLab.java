package com.tenuchon.libr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tenuchon.libr.database.BookBaseHelper;
import com.tenuchon.libr.database.BookCursorWrapper;
import com.tenuchon.libr.database.BookDBScheme;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.tenuchon.libr.database.BookDBScheme.LibraryTable.*;

public class BookLab {
    private static BookLab sBookLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private BookLab(Context context) {
        mContext = context;
        mDatabase = new BookBaseHelper(context).getWritableDatabase();
    }

    public static BookLab get(Context context) {
        if (sBookLab == null) {
            sBookLab = new BookLab(context);
            return sBookLab;
        }
        return sBookLab;
    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        try (BookCursorWrapper cursor = queryBooks(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                books.add(cursor.getBook());
                cursor.moveToNext();
            }
        }
        return books;
    }

    public Book getBook(UUID id) {
        try (BookCursorWrapper cursor = queryBooks(
                Cols.UUID + " = ?",
                new String[]{id.toString()}
        )) {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getBook();
        }
    }

    public void addBook(Book book) {
        ContentValues values = getContentValues(book);
        mDatabase.insert(NAME, null, values);
    }

    public void removeBook(Book book) {
        String uuidString = book.getId().toString();
        mDatabase.delete(NAME, Cols.UUID + " = ?", new String[]{uuidString});
    }

    public void updateBook(Book book) {
        String uuidString = book.getId().toString();
        ContentValues values = getContentValues(book);
        mDatabase.update(NAME, values, Cols.UUID + "= ?", new String[]{uuidString});
    }

    public BookCursorWrapper queryBooks(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(NAME, null, whereClause, whereArgs,
                null, null, null);
        return new BookCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Book book) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, book.getId().toString());
        values.put(Cols.TITLE, book.getTitle());
        values.put(Cols.AUTHOR, book.getAuthor());
        values.put(Cols.DESCRIPTION, book.getDescription());
        values.put(Cols.FAVORITE, book.isFavorite() ? 1 : 0);
        return values;
    }

}
