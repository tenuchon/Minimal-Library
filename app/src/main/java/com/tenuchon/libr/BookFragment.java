package com.tenuchon.libr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import java.util.Objects;
import java.util.UUID;

public class BookFragment extends DefaultFragment implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    public static final String TAG = "BookFragment";
    private static final String ARG_BOOK_ID = "book_id";
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mImage;
    private ImageButton mButtonBack;
    private TextView mDescription, mAuthorCollapsingToolbar;
    private Book mBook;
    private BottomAppBar mBottomAppBar;
    private FloatingActionButton mFab;
    private MenuItem mMenuItem;

    public static Fragment newInstance(UUID bookId) {
        Bundle arg = new Bundle();
        arg.putSerializable(ARG_BOOK_ID, bookId);

        BookFragment bookFragment = new BookFragment();
        bookFragment.setArguments(arg);
        return bookFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID bookId = null;
        if (getArguments() != null) {
            bookId = (UUID) getArguments().getSerializable(ARG_BOOK_ID);
        }
        if (bookId != null) {
            mBook = BookLab.get(getActivity()).getBook(bookId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        mCollapsingToolbarLayout = view.findViewById(R.id.collapsingToolbarLayout);
        mImage = mCollapsingToolbarLayout.findViewById(R.id.iv_image_collapsing_toolbar);
        mAuthorCollapsingToolbar = mCollapsingToolbarLayout.findViewById(R.id.tv_author_collapsing_toolbar);
        mButtonBack = mCollapsingToolbarLayout.findViewById(R.id.ib_back);
        mDescription = view.findViewById(R.id.tv_description);
        mBottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        mFab = getActivity().findViewById(R.id.fab);

        mButtonBack.setOnClickListener(this);
        mDescription.setText(mBook.getDescription());
        mCollapsingToolbarLayout.setTitle(mBook.getTitle());
        mAuthorCollapsingToolbar.setText(mBook.getAuthor());

        mBottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        mFab.setImageResource(R.drawable.ic_change);
        mBottomAppBar.replaceMenu(R.menu.book_bottom_app_bar_menu);
        mBottomAppBar.setOnMenuItemClickListener(this);
        mFab.setOnClickListener(this);

        Menu menu = mBottomAppBar.getMenu();
        mMenuItem = menu.findItem(R.id.favorite);
        if (mBook.isFavorite())
        mMenuItem.setIcon(ContextCompat.getDrawable(getActivity(),R.drawable.ic_favorite));

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                onBackClick();
                break;
            case R.id.fab:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,EditFragment.newInstance(mBook.getId()))
                        .addToBackStack(EditFragment.TAG).commit();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                BookLab.get(getActivity()).removeBook(mBook);
                onBackClick();
                break;
            case R.id.share:
                String shareText = getString(R.string.share_text,mBook.getTitle(),mBook.getAuthor());
                ShareCompat.IntentBuilder.from((getActivity()))
                        .setType("text/plain")
                        .setText(shareText)
                        .setChooserTitle(getString(R.string.share))
                        .startChooser();
                break;
            case R.id.favorite:
                boolean isFavorite = mBook.isFavorite();
                if (isFavorite){
                    mBook.setFavorite(false);
                    mMenuItem.setIcon(ContextCompat.getDrawable(getActivity(),R.drawable.ic_not_favorite));
                    showSnackbar(true);
                }else {
                    mBook.setFavorite(true);
                    mMenuItem.setIcon(ContextCompat.getDrawable(getActivity(),R.drawable.ic_favorite));
                    showSnackbar(false);
                }
                BookLab.get(getActivity()).updateBook(mBook);
                break;
        }
        return true;
    }

    private void showSnackbar(boolean isFavorite){
        String text;
        if (isFavorite) text = getString(R.string.removed_favorites_snackbar);
        else text = getString(R.string.add_favorites_snackbar);

        Snackbar.make(getView().findViewById(R.id.fragment_book_layout),text,Snackbar.LENGTH_SHORT)
                .setAnchorView(getActivity().findViewById(R.id.fab)).show();
    }

}
