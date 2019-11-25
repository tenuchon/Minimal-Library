package com.tenuchon.minimallibrary.utils;

import android.os.Build;
import android.text.format.DateFormat;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.tenuchon.minimallibrary.R;
import com.tenuchon.minimallibrary.database.model.Book;
import com.tenuchon.minimallibrary.ui.book.BookFragment;
import com.tenuchon.minimallibrary.ui.edit.EditFragment;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> implements Filterable {
    private Book book;
    private int bookDeletePosition;
    private List<Book> books;
    private List<Book> booksCopy;
    private FragmentManager fragmentManager;
    private View rootLayout;

    public BookAdapter(List<Book> books, FragmentManager fragmentManager) {
        this.books = books;
        this.fragmentManager = fragmentManager;
        booksCopy = new ArrayList<>(books);
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new BookHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
        ViewCompat.setTransitionName(holder.photoImageView,
                position + "_image");
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setRootLayout(View rootLayout) {
        this.rootLayout = rootLayout;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void deleteItem(int position) {
        book = books.get(position);
        bookDeletePosition = position;
        books.remove(position);
        BookLab.get(rootLayout.getContext()).removeBook(book);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        Snackbar snackbar = Snackbar.make(rootLayout, R.string.snackbar_removed_book,
                Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(rootLayout.getRootView().findViewById(R.id.fab));
        snackbar.setAction(R.string.snackbar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoDelete();
            }
        });
        snackbar.setActionTextColor(rootLayout.getContext().getResources().getColor(R.color.snackbarButton));
        snackbar.show();
    }

    private void undoDelete() {
        BookLab.get(rootLayout.getContext()).addBook(book);
        books.add(bookDeletePosition, book);
        notifyItemInserted(bookDeletePosition);
    }

    @Override
    public Filter getFilter() {
        return booksFilter;
    }

    private Filter booksFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(booksCopy);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Book book : booksCopy) {
                    if (book.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(book);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            books.clear();
            books.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String dateFormat = "dd.MM.yy";

        private Book book;
        private TextView titleTextView, authorTextView, dateTextView;
        private Button detailsButton, editButton;
        private ImageView photoImageView;

        private BookHolder(@NonNull LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.card_book, parent, false));

            titleTextView = itemView.findViewById(R.id.tv_title_card);
            authorTextView = itemView.findViewById(R.id.tv_author_card);
            dateTextView = itemView.findViewById(R.id.tv_date);
            detailsButton = itemView.findViewById(R.id.bt_details_card);
            editButton = itemView.findViewById(R.id.bt_edit_card);
            photoImageView = itemView.findViewById(R.id.iv_photo_card_book);

            detailsButton.setOnClickListener(this);
            editButton.setOnClickListener(this);
        }

        private void bind(Book book) {
            this.book = book;
            titleTextView.setText(book.getTitle());
            authorTextView.setText(String.format(authorTextView.getContext()
                    .getResources()
                    .getString(R.string.author_card_book), book.getAuthor()));
            dateTextView.setText(DateFormat.format(dateFormat, book.getDate()));
            ImageHelper.loadImageAndSetView(BookLab.get(rootLayout.getContext()).getPhotoFile(book),
                    photoImageView, false);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_details_card:
                    goToFragment(BookFragment.newInstance(book.getId()), BookFragment.TAG);
                    break;
                case R.id.bt_edit_card:
                    goToFragment(EditFragment.newInstance(book.getId()), EditFragment.TAG);

            }
        }

        private void goToFragment(Fragment fragment, String tag) {
            if (tag.equals(BookFragment.TAG)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    fragment.setSharedElementEnterTransition(new DetailsTransition());
                    fragment.setEnterTransition(new Fade());
                    fragment.setExitTransition(new Fade());
                    fragment.setSharedElementReturnTransition(new DetailsTransition());
                }
                fragmentManager.beginTransaction()
                        .addSharedElement(photoImageView, "photoBook")
                        .replace(R.id.fragment_container,
                                fragment)
                        .addToBackStack(tag).commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,
                                fragment)
                        .addToBackStack(tag).commit();
            }
        }
    }
}
