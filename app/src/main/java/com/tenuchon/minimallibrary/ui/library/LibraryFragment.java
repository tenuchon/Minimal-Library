package com.tenuchon.minimallibrary.ui.library;

import androidx.fragment.app.Fragment;

import com.tenuchon.minimallibrary.R;
import com.tenuchon.minimallibrary.database.model.Book;
import com.tenuchon.minimallibrary.ui.BasicListFragment;
import com.tenuchon.minimallibrary.utils.BookLab;

import java.util.List;

public class LibraryFragment extends BasicListFragment {

    public static final String TAG = "LibraryFragment";

    public static Fragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public String getTitleToolbar() {
        return getString(R.string.library);
    }

    @Override
    public List<Book> getBooks() {
        return BookLab.get(getActivity()).getLibraryBooks();
    }

    @Override
    public String getStringTag() {
        return TAG;
    }

}
