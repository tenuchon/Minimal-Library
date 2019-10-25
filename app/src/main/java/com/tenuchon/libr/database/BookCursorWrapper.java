package com.tenuchon.libr.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.tenuchon.libr.Book;

import java.util.UUID;

import static com.tenuchon.libr.database.BookDBScheme.*;

public class BookCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public BookCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Book getBook(){
        String uuidString = getString(getColumnIndex(LibraryTable.Cols.UUID));
        String title = getString(getColumnIndex(LibraryTable.Cols.TITLE));
        String author = getString(getColumnIndex(LibraryTable.Cols.AUTHOR));
        String description = getString(getColumnIndex(LibraryTable.Cols.DESCRIPTION));
        int isFavorite = getInt(getColumnIndex(LibraryTable.Cols.FAVORITE));

        Book book = new Book(UUID.fromString(uuidString));
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setFavorite(isFavorite != 0);

        return book;
    }
}
