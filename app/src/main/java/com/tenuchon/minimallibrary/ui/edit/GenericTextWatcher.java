package com.tenuchon.minimallibrary.ui.edit;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.tenuchon.minimallibrary.R;
import com.tenuchon.minimallibrary.database.model.Book;

public class GenericTextWatcher implements TextWatcher {
    private TextInputLayout textLayout;
    private EditText editText;
    private Book book;
    private String errorMessage;

    public GenericTextWatcher(TextInputLayout textLayout, EditText editText, Book book, String errorMessage) {
        this.textLayout = textLayout;
        this.editText = editText;
        this.book = book;
        this.errorMessage = errorMessage;
    }

    public GenericTextWatcher(EditText editText, Book book) {
        this.editText = editText;
        this.book = book;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (textLayout != null) textLayout.setError(null);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        switch (editText.getId()) {
            case R.id.et_title_edit:
                book.setTitle(s.toString());
                if (s.length() == 0) textLayout.setError(errorMessage);
                break;
            case R.id.et_author_edit:
                book.setAuthor(s.toString());
                if (s.length() == 0) textLayout.setError(errorMessage);
                break;
            case R.id.et_description_edit:
                book.setDescription(s.toString());
                break;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
