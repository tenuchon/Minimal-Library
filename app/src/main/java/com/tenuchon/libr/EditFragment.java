package com.tenuchon.libr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.UUID;

public class EditFragment extends DefaultFragment implements View.OnClickListener {
    public static final String TAG = "edit_fragment";
    public static final String ARG_BOOK_ID = "book_id";
    private String errorMessage;
    private Book mBook;
    private TextInputLayout mTextInputLayoutTitle, mTextInputLayoutAuthor, mTextInputLayoutDescription;
    private EditText mEditTextTitle, mEditTextAuthor, mEditTextDescription;
    private ImageButton mImageButtonCancel;
    private Button mButtonSave;
    private Toolbar mToolbar;
    private BottomAppBar mBottomAppBar;
    private FloatingActionButton mFab;
    private boolean isNewBook = true;

    public static EditFragment newInstance(UUID bookId) {
        Bundle arg = new Bundle();
        arg.putSerializable(ARG_BOOK_ID, bookId);

        EditFragment editFragment = new EditFragment();
        editFragment.setArguments(arg);

        return editFragment;
    }

    public static EditFragment newInstance() {
        return new EditFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        errorMessage = getResources().getString(R.string.error_message_edit_text);

        if (getArguments() != null) {
            UUID bookId = (UUID) getArguments().getSerializable(ARG_BOOK_ID);
            mBook = BookLab.get(getActivity()).getBook(bookId);
            isNewBook = false;
        } else {
            mBook = new Book();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        mTextInputLayoutTitle = view.findViewById(R.id.til_title);
        mTextInputLayoutAuthor = view.findViewById(R.id.til_author);
        mTextInputLayoutDescription = view.findViewById(R.id.til_description);
        mEditTextTitle = view.findViewById(R.id.et_title_edit);
        mEditTextAuthor = view.findViewById(R.id.et_author_edit);
        mEditTextDescription = view.findViewById(R.id.et_description_edit);
        mToolbar = view.findViewById(R.id.toolbar);
        mImageButtonCancel = mToolbar.findViewById(R.id.ib_toolbar_cancel);
        mButtonSave = mToolbar.findViewById(R.id.bt_toolbar_save);
        mBottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        mFab = getActivity().findViewById(R.id.fab);

        mImageButtonCancel.setOnClickListener(this);
        mButtonSave.setOnClickListener(this);
        mFab.setOnClickListener(this);
        mEditTextTitle.addTextChangedListener(
                new GenericTextWatcher(mTextInputLayoutTitle, mEditTextTitle, mBook, errorMessage));
        mEditTextAuthor.addTextChangedListener(
                new GenericTextWatcher(mTextInputLayoutAuthor, mEditTextAuthor, mBook, errorMessage));
        mEditTextDescription.addTextChangedListener(
                new GenericTextWatcher(mEditTextDescription, mBook));

        mBottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        mBottomAppBar.replaceMenu(R.menu.edit_bottom_app_bar_menu);
        mFab.setImageResource(R.drawable.ic_delete);

        if (!isNewBook) {
            mEditTextTitle.setText(mBook.getTitle());
            mEditTextAuthor.setText(mBook.getAuthor());
            if (mBook.getDescription() != null) {
                mEditTextDescription.setText(mBook.getDescription());
            }
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_toolbar_cancel:
                onBackClick();
                break;
            case R.id.bt_toolbar_save:
                if (mEditTextTitle.getText().toString().length() != 0 && mEditTextAuthor.getText().toString().length() != 0) {
                    if (isNewBook) BookLab.get(getActivity()).addBook(mBook);
                    else BookLab.get(getActivity()).updateBook(mBook);
                    onBackClick();
                }
                if (mEditTextTitle.getText().toString().length() == 0)
                    mTextInputLayoutTitle.setError(errorMessage);
                if (mEditTextAuthor.getText().toString().length() == 0)
                    mTextInputLayoutAuthor.setError(errorMessage);
                break;
            case R.id.fab:
                mEditTextTitle.getText().clear();
                mEditTextAuthor.getText().clear();
                mEditTextDescription.getText().clear();
                break;
        }
    }

}
