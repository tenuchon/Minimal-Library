package com.tenuchon.libr;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {
    private Book mBook;
    private int mBookDeletePosition;
    private List<Book> mBooks;
    private FragmentManager mFragmentManager;
    private Activity mActivity;

    public BookAdapter(List<Book> books, FragmentManager fragmentManager, Activity activity) {
        mBooks = books;
        mFragmentManager = fragmentManager;
        mActivity = activity;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new BookHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        Book book = mBooks.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public void setBooks(List<Book> books) {
        mBooks = books;
    }

    public void deleteItem(int position) {
        mBook = mBooks.get(position);
        mBookDeletePosition = position;
        mBooks.remove(position);

        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = mActivity.findViewById(R.id.coordinator_layout);
        Snackbar snackbar = Snackbar.make(view, R.string.snackbar_removed_book,
                Snackbar.LENGTH_LONG);
        snackbar.setAnchorView(mActivity.findViewById(R.id.fab));
        snackbar.setAction(R.string.snackbar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoDelete();
            }
        });
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (event != DISMISS_EVENT_ACTION)
                    BookLab.get(mActivity).removeBook(mBook);
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        mBooks.add(mBookDeletePosition,
                mBook);
        notifyItemInserted(mBookDeletePosition);
    }

    class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Book mBook;
        private TextView mTextViewTitle, mTextViewAuthor;
        private Button mButtonDetails, mButtonEdit;


        private BookHolder(@NonNull LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.card_book, parent, false));

            mTextViewTitle = itemView.findViewById(R.id.tv_title_card);
            mTextViewAuthor = itemView.findViewById(R.id.tv_author_card);
            mButtonDetails = itemView.findViewById(R.id.bt_details_card);
            mButtonEdit = itemView.findViewById(R.id.bt_edit_card);

            mButtonDetails.setOnClickListener(this);
            mButtonEdit.setOnClickListener(this);
        }

        private void bind(Book book) {
            mBook = book;
            mTextViewTitle.setText(book.getTitle());
            mTextViewAuthor.setText(book.getAuthor());
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_details_card:
                    mFragmentManager.beginTransaction().replace(R.id.fragment_container, BookFragment.newInstance(mBook.getId()))
                            .addToBackStack(BookFragment.TAG).commit();
                    break;
                case R.id.bt_edit_card:
                    mFragmentManager.beginTransaction().replace(R.id.fragment_container, EditFragment.newInstance(mBook.getId()))
                            .addToBackStack(EditFragment.TAG).commit();
            }
        }
    }
}
