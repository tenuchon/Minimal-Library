package com.tenuchon.minimallibrary.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tenuchon.minimallibrary.database.BookBaseHelper;
import com.tenuchon.minimallibrary.database.BookCursorWrapper;
import com.tenuchon.minimallibrary.database.model.Book;
import com.tenuchon.minimallibrary.database.BookDBScheme;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class BookLab {
    private static BookLab bookLab;
    private Context context;
    private SQLiteDatabase database;

    private BookLab(Context context) {
        this.context = context;
        database = new BookBaseHelper(context).getWritableDatabase();
    }

    public static BookLab get(Context context) {
        if (bookLab == null) {
            synchronized (BookLab.class) {
                if (bookLab == null) {
                    bookLab = new BookLab(context);
                }
            }
        }
        return bookLab;
    }

    public List<Book> getLibraryBooks() {
        return getBooks(BookDBScheme.LibraryTable.Cols.WISH + " = 0", null);
    }

    public List<Book> getFavoritesBooks() {
        return getBooks(BookDBScheme.LibraryTable.Cols.FAVORITE + " = 1", null);
    }

    public List<Book> getWishBooks() {
        return getBooks(BookDBScheme.LibraryTable.Cols.WISH + " = 1", null);
    }

    private List<Book> getBooks(String whereClause, String[] whereArgs) {
        List<Book> books = new ArrayList<>();
        try (BookCursorWrapper cursor = queryBooks(whereClause, whereArgs)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                books.add(cursor.getBook());
                cursor.moveToNext();
            }
        }
        return books;
    }

    public List<Book> getBooksSortedByTitle(List<Book> books) {
        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
            }
        });
        return books;
    }

    public List<Book> getBooksSortedByAuthor(List<Book> books) {
        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.getAuthor().toLowerCase().compareTo(o2.getAuthor().toLowerCase());
            }
        });
        return books;
    }

    public List<Book> getBooksSortedByDate(List<Book> books) {
        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        return books;
    }

    public File getPhotoFile(Book book) {
        File filesDir = context.getFilesDir();
        return new File(filesDir, book.getPhotoFilename());
    }

    public Book getBook(UUID id) {
        try (BookCursorWrapper cursor = queryBooks(
                BookDBScheme.LibraryTable.Cols.UUID + " = ?",
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
        database.insert(BookDBScheme.LibraryTable.NAME, null, values);
    }

    public void removeBook(Book book) {
        String uuidString = book.getId().toString();
        database.delete(BookDBScheme.LibraryTable.NAME, BookDBScheme.LibraryTable.Cols.UUID + " = ?", new String[]{uuidString});
        ImageHelper.deleteImage(context, this.getPhotoFile(book));
    }

    public void updateBook(Book book) {
        String uuidString = book.getId().toString();
        ContentValues values = getContentValues(book);
        database.update(BookDBScheme.LibraryTable.NAME, values, BookDBScheme.LibraryTable.Cols.UUID + "= ?", new String[]{uuidString});
    }

    public BookCursorWrapper queryBooks(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(BookDBScheme.LibraryTable.NAME, null, whereClause, whereArgs,
                null, null, null);
        return new BookCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Book book) {
        ContentValues values = new ContentValues();
        values.put(BookDBScheme.LibraryTable.Cols.UUID, book.getId().toString());
        values.put(BookDBScheme.LibraryTable.Cols.DATE, book.getDate().getTime());
        values.put(BookDBScheme.LibraryTable.Cols.TITLE, book.getTitle());
        values.put(BookDBScheme.LibraryTable.Cols.AUTHOR, book.getAuthor());
        values.put(BookDBScheme.LibraryTable.Cols.DESCRIPTION, book.getDescription());
        values.put(BookDBScheme.LibraryTable.Cols.FAVORITE, book.isFavorite() ? 1 : 0);
        values.put(BookDBScheme.LibraryTable.Cols.WISH, book.isWish() ? 1 : 0);
        return values;
    }
}
