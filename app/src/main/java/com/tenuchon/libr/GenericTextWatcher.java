package com.tenuchon.libr;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class GenericTextWatcher implements TextWatcher {
    private TextInputLayout mTextInputLayout;
    private EditText mEditText;
    private Book mBook;
    private String mErrorMessage;

    public GenericTextWatcher(TextInputLayout textInputLayout, EditText editText, Book book, String errorMessage) {
        mTextInputLayout = textInputLayout;
        mEditText = editText;
        mBook = book;
        mErrorMessage = errorMessage;
    }

    public GenericTextWatcher(EditText editText, Book book) {
        mEditText = editText;
        mBook = book;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (mTextInputLayout != null) mTextInputLayout.setError(null);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        switch (mEditText.getId()) {
            case R.id.et_title_edit:
                mBook.setTitle(s.toString());
                if (s.length() == 0) mTextInputLayout.setError(mErrorMessage);
                break;
            case R.id.et_author_edit:
                mBook.setAuthor(s.toString());
                if (s.length() == 0) mTextInputLayout.setError(mErrorMessage);
                break;
            case R.id.et_description_edit:
                mBook.setDescription(s.toString());
                break;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
