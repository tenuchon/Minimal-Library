package com.tenuchon.minimallibrary.ui.wishlist;


import com.tenuchon.minimallibrary.R;
import com.tenuchon.minimallibrary.database.model.Book;
import com.tenuchon.minimallibrary.ui.BasicListFragment;
import com.tenuchon.minimallibrary.utils.BookLab;

import java.util.List;

public class WishListFragment extends BasicListFragment {
    public static final String TAG = "WishListFragment";

    public static WishListFragment newInstance() {
        return new WishListFragment();
    }

    @Override
    public String getTitleToolbar() {
        return getString(R.string.wish_list);
    }

    @Override
    public List<Book> getBooks() {
        return BookLab.get(getActivity()).getWishBooks();
    }

    @Override
    public String getStringTag() {
        return TAG;
    }


}
