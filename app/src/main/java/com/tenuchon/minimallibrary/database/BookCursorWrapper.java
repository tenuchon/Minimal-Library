package com.tenuchon.minimallibrary.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.tenuchon.minimallibrary.database.model.Book;

import java.util.Date;
import java.util.UUID;

import static com.tenuchon.minimallibrary.database.BookDBScheme.*;

public class BookCursorWrapper extends CursorWrapper {

    public BookCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Book getBook(){
        String uuidString = getString(getColumnIndex(LibraryTable.Cols.UUID));
        long date = getLong(getColumnIndex(LibraryTable.Cols.DATE));
        String title = getString(getColumnIndex(LibraryTable.Cols.TITLE));
        String author = getString(getColumnIndex(LibraryTable.Cols.AUTHOR));
        String description = getString(getColumnIndex(LibraryTable.Cols.DESCRIPTION));
        int isFavorite = getInt(getColumnIndex(LibraryTable.Cols.FAVORITE));
        int isWish = getInt(getColumnIndex(LibraryTable.Cols.WISH));

        Book book = new Book(UUID.fromString(uuidString));
        book.setDate(new Date(date));
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setFavorite(isFavorite!= 0);
        book.setWish(isWish!=0);

        return book;
    }
}
