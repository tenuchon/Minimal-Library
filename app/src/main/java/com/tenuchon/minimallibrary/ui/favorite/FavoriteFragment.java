package com.tenuchon.minimallibrary.ui.favorite;

import com.tenuchon.minimallibrary.R;
import com.tenuchon.minimallibrary.database.model.Book;
import com.tenuchon.minimallibrary.ui.BasicListFragment;
import com.tenuchon.minimallibrary.utils.BookLab;

import java.util.List;

public class FavoriteFragment extends BasicListFragment {
    public static final String TAG = "FavoriteFragment";

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public String getTitleToolbar() {
        return getString(R.string.favorite);
    }

    @Override
    public List<Book> getBooks() {
        return BookLab.get(getActivity()).getFavoritesBooks();
    }

    @Override
    public String getStringTag() {
        return TAG;
    }

}
