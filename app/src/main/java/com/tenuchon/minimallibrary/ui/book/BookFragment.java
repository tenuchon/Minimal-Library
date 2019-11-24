package com.tenuchon.minimallibrary.ui.book;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tenuchon.minimallibrary.R;
import com.tenuchon.minimallibrary.utils.BookLab;
import com.tenuchon.minimallibrary.database.model.Book;
import com.tenuchon.minimallibrary.ui.DefaultFragment;
import com.tenuchon.minimallibrary.ui.edit.EditFragment;
import com.tenuchon.minimallibrary.utils.ImageHelper;


import java.io.File;
import java.util.UUID;

public class BookFragment extends DefaultFragment implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    public static final String TAG = "BookFragment";
    private static final String ARG_BOOK_ID = "book_id";

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView photoImageView;
    private ImageButton backFragmentButton;
    private TextView descriptionTextView, authorTextView;
    private Book book;
    private File photoFile;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;
    private MenuItem menuItem;

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
        BookLab bookLab = BookLab.get(getActivity());
        if (getArguments() != null) {
            UUID bookId = (UUID) getArguments().getSerializable(ARG_BOOK_ID);
            book = bookLab.getBook(bookId);
            photoFile = bookLab.getPhotoFile(book);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbarLayout);
        photoImageView = collapsingToolbarLayout.findViewById(R.id.iv_image_collapsing_toolbar);
        authorTextView = collapsingToolbarLayout.findViewById(R.id.tv_author_collapsing_toolbar);
        backFragmentButton = collapsingToolbarLayout.findViewById(R.id.ib_back);
        descriptionTextView = view.findViewById(R.id.tv_description);
        bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        fab = getActivity().findViewById(R.id.fab);

        backFragmentButton.setOnClickListener(this);
        bottomAppBar.setOnMenuItemClickListener(this);
        fab.setOnClickListener(this);

        setFields();
        updateBottomAppBar();

        Menu menu = bottomAppBar.getMenu();
        menuItem = menu.findItem(R.id.favorite);
        if (book.isFavorite()) menuItem.setIcon(R.drawable.ic_favorite);
        return view;
    }

    private void updateBottomAppBar() {
        bottomAppBar.replaceMenu(R.menu.book_bottom_app_bar_menu);
        bottomAppBar.performShow();
        fab.setImageResource(R.drawable.ic_change);
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
    }

    private void setFields() {
        collapsingToolbarLayout.setTitle(book.getTitle());
        authorTextView.setText(book.getAuthor());
        descriptionTextView.setText(book.getDescription());
        ImageHelper.loadImageAndSetView(photoFile, photoImageView, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                onBackClick();
                break;
            case R.id.fab:
                goToEditFragment();
                break;
        }
    }

    private void goToEditFragment() {
        FragmentManager fm = getFragmentManager();
        if (fm != null){
            fm.beginTransaction()
                    .replace(R.id.fragment_container,EditFragment.newInstance(book.getId()))
                    .addToBackStack(EditFragment.TAG)
                    .commit();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                showDialogDelete();
                break;
            case R.id.share:
                share();
                break;
            case R.id.favorite:
                changeFavorite();
                break;
        }
        return true;
    }

    private void showDialogDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.delete_alert_dialog_message))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BookLab.get(getActivity()).removeBook(book);
                        onBackClick();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create()
                .show();
    }

    private void share() {
        String shareText = getString(R.string.share_text, book.getTitle(), book.getAuthor());
        ShareCompat.IntentBuilder.from((getActivity()))
                .setType("text/plain")
                .setText(shareText)
                .setChooserTitle(getString(R.string.share))
                .startChooser();
    }

    private void changeFavorite() {
        boolean isFavorite = book.isFavorite();
        if (isFavorite) {
            book.setFavorite(false);
            menuItem.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_not_favorite));
            showSnackbar(true);
        } else {
            book.setFavorite(true);
            menuItem.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite));
            showSnackbar(false);
        }
        BookLab.get(getActivity()).updateBook(book);
    }

    private void showSnackbar(boolean isFavorite) {
        String text;
        if (isFavorite) text = getString(R.string.removed_favorites_snackbar);
        else text = getString(R.string.add_favorites_snackbar);

        Snackbar.make(getView().findViewById(R.id.fragment_book_layout), text, Snackbar.LENGTH_SHORT)
                .setAnchorView(getActivity().findViewById(R.id.fab)).show();
    }

}
