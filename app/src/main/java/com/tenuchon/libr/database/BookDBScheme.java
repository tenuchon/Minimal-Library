package com.tenuchon.libr.database;

public class BookDBScheme {
    public final static class LibraryTable {
        public static final String NAME = "library";

        public final static class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String AUTHOR = "author";
            public static final String DESCRIPTION = "description";
            public static final String FAVORITE = "favorite";

        }
    }
}
