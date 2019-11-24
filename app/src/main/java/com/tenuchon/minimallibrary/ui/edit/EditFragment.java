package com.tenuchon.minimallibrary.ui.edit;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.tenuchon.minimallibrary.R;
import com.tenuchon.minimallibrary.database.model.Book;
import com.tenuchon.minimallibrary.ui.favorite.FavoriteFragment;
import com.tenuchon.minimallibrary.ui.library.LibraryFragment;
import com.tenuchon.minimallibrary.ui.wishlist.WishListFragment;
import com.tenuchon.minimallibrary.utils.BookLab;
import com.tenuchon.minimallibrary.ui.DefaultFragment;
import com.tenuchon.minimallibrary.utils.ImageHelper;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class EditFragment extends DefaultFragment implements View.OnClickListener {
    public static final String TAG = "EditFragment";

    private static final String FRAGMENT_TAG = "fragment_tag";
    private static final String ARG_BOOK_ID = "book_id";
    private static final String PHOTO_FILE = "photo_file";

    private static final int REQUEST_PHOTO = 1;
    private static final int FROM_LIBRARY = 2;
    private static final int FROM_FAVORITE = 3;
    private static final int FROM_WISH_LIST = 4;
    private static final int EDIT = 5;

    private int flag;
    private boolean saveButtonPressed;
    private boolean isNewBook = true;

    private String errorMessage, buttonChangePhotoText, buttonAddPhotoText;
    private Book book;
    private File photoFile;
    private TextInputLayout titleLayout, authorLayout, descriptionLayout;
    private EditText titleEditText, authorEditText, descriptionEditText;
    private ImageButton cancelImageButton;
    private Button saveButton, addPhotoButton;
    private ImageView photoImageView;
    private Toolbar toolbar;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;
    private Unregistrar unregistrar;


    public static EditFragment newInstance(UUID bookId) {
        Bundle arg = new Bundle();
        arg.putSerializable(ARG_BOOK_ID, bookId);

        EditFragment editFragment = new EditFragment();
        editFragment.setArguments(arg);
        return editFragment;
    }

    public static EditFragment newInstance(String tag) {
        Bundle arg = new Bundle();
        arg.putString(FRAGMENT_TAG, tag);

        EditFragment editFragment = new EditFragment();
        editFragment.setArguments(arg);
        return editFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        errorMessage = getString(R.string.error_message_edit_text);
        buttonChangePhotoText = getString(R.string.change_photo);
        buttonAddPhotoText = getString(R.string.button_add_photo);
        setRetainInstance(true);

        if (getArguments() != null) {
            if (getArguments().getString(FRAGMENT_TAG) != null) {
                String key = getArguments().getString(FRAGMENT_TAG);
                switch (key) {
                    case LibraryFragment.TAG:
                        flag = FROM_LIBRARY;
                        break;
                    case FavoriteFragment.TAG:
                        flag = FROM_FAVORITE;
                        break;
                    case WishListFragment.TAG:
                        flag = FROM_WISH_LIST;
                        break;
                }
                book = new Book();
            } else {
                UUID bookId = (UUID) getArguments().getSerializable(ARG_BOOK_ID);
                book = BookLab.get(getActivity()).getBook(bookId);
                photoFile = BookLab.get(getActivity()).getPhotoFile(book);
                isNewBook = false;
                flag = EDIT;
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        titleLayout = view.findViewById(R.id.til_title);
        authorLayout = view.findViewById(R.id.til_author);
        descriptionLayout = view.findViewById(R.id.til_description);
        titleEditText = view.findViewById(R.id.et_title_edit);
        authorEditText = view.findViewById(R.id.et_author_edit);
        descriptionEditText = view.findViewById(R.id.et_description_edit);
        toolbar = view.findViewById(R.id.toolbar);
        cancelImageButton = toolbar.findViewById(R.id.ib_toolbar_cancel);
        photoImageView = view.findViewById(R.id.iv_photo_book);
        saveButton = toolbar.findViewById(R.id.bt_toolbar_save);
        addPhotoButton = view.findViewById(R.id.bt_add_photo);
        bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        fab = getActivity().findViewById(R.id.fab);


        cancelImageButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        addPhotoButton.setOnClickListener(this);
        fab.setOnClickListener(this);
        titleEditText.addTextChangedListener(
                new GenericTextWatcher(titleLayout, titleEditText, book, errorMessage));
        authorEditText.addTextChangedListener(
                new GenericTextWatcher(authorLayout, authorEditText, book, errorMessage));
        descriptionEditText.addTextChangedListener(
                new GenericTextWatcher(descriptionEditText, book));

        unregistrar = KeyboardVisibilityEvent.registerEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        setVisibilityBotAppBarAndFab(isOpen);
                    }
                });

        setFields();
        updateBottomAppBar();
        updatePhotoView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setVisibilityBotAppBarAndFab(false);
        unregistrar.unregister();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isNewBook && !saveButtonPressed && photoFile != null) deletePhoto();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PHOTO_FILE, photoFile);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            photoFile = (File) savedInstanceState.getSerializable(PHOTO_FILE);
        }
    }

    private void setVisibilityBotAppBarAndFab(boolean visibility) {
        if (visibility) {
            bottomAppBar.setVisibility(View.INVISIBLE);
            fab.setVisibility(View.INVISIBLE);
        } else {
            bottomAppBar.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
        }
    }

    private void setFields() {
        if (!isNewBook) {
            titleEditText.setText(book.getTitle());
            authorEditText.setText(book.getAuthor());
            if (book.getDescription() != null) {
                descriptionEditText.setText(book.getDescription());
            }
        }
    }

    private void updateBottomAppBar() {
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        bottomAppBar.replaceMenu(R.menu.empty_bottom_app_bar_menu);
        bottomAppBar.performShow();
        fab.setImageResource(R.drawable.ic_clear);
    }

    private void updatePhotoView() {
        if (photoFile == null || !photoFile.exists()) addPhotoButton.setText(buttonAddPhotoText);
        else addPhotoButton.setText(buttonChangePhotoText);
        ImageHelper.loadImageAndSetView(photoFile, photoImageView, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_toolbar_cancel:
                onBackClick();
                break;
            case R.id.bt_toolbar_save:
                saveBook();
                break;
            case R.id.fab:
                clearField();
                break;
            case R.id.bt_add_photo:
                addPhoto();
                break;
        }
    }

    private void addPhoto() {
        deletePhoto();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = BookLab.get(getActivity()).getPhotoFile(book);
        Uri uri = ImageHelper.getUriImage(getActivity(), photoFile);
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        List<ResolveInfo> cameraActivities = getActivity()
                .getPackageManager().queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo activity : cameraActivities) {
            getActivity().grantUriPermission(activity.activityInfo.packageName,
                    uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(captureImage, REQUEST_PHOTO);
    }

    private void saveBook() {
        int lengthTitle = titleEditText.getText().toString().length();
        int lengthAuthor = authorEditText.getText().toString().length();

        if (lengthTitle != 0 && lengthAuthor != 0) {
            BookLab bookLab = BookLab.get(getActivity());
            switch (flag) {
                case FROM_LIBRARY:
                    bookLab.addBook(book);
                    break;
                case FROM_FAVORITE:
                    book.setFavorite(true);
                    bookLab.addBook(book);
                    break;
                case FROM_WISH_LIST:
                    book.setWish(true);
                    bookLab.addBook(book);
                    break;
                case EDIT:
                    bookLab.updateBook(book);
                    break;
            }
            saveButtonPressed = true;
            onBackClick();
        }

        if (lengthTitle == 0) titleLayout.setError(errorMessage);
        if (lengthAuthor == 0) authorLayout.setError(errorMessage);
    }

    private void clearField() {
        titleEditText.getText().clear();
        authorEditText.getText().clear();
        descriptionEditText.getText().clear();
        photoFile = null;
        updatePhotoView();
    }

    private void deletePhoto() {
        if (photoFile != null) {
            ImageHelper.deleteImage(getActivity(), photoFile);
            photoFile = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_PHOTO) {
            Uri uri = ImageHelper.getUriImage(getActivity(), photoFile);
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
    }

}
