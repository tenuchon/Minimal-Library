package com.tenuchon.minimallibrary.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tenuchon.minimallibrary.R;
import com.tenuchon.minimallibrary.database.model.Book;
import com.tenuchon.minimallibrary.ui.edit.EditFragment;
import com.tenuchon.minimallibrary.utils.BookAdapter;
import com.tenuchon.minimallibrary.utils.BookLab;
import com.tenuchon.minimallibrary.utils.InitApplication;
import com.tenuchon.minimallibrary.utils.SwipeToDeleteCallback;

import java.util.List;

public abstract class BasicListFragment extends DefaultFragment implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private Toolbar toolbar;
    private View rootLayout;
    private LinearLayout listEmptyView;
    private TextView titleToolbar;

    public abstract String getTitleToolbar();

    public abstract List<Book> getBooks();

    public abstract String getStringTag();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.rv_list);
        toolbar = view.findViewById(R.id.toolbar);
        bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        fab = getActivity().findViewById(R.id.fab);
        rootLayout = view.findViewById(R.id.root_layout);
        listEmptyView = view.findViewById(R.id.ll_list_empty);
        titleToolbar = toolbar.findViewById(R.id.tv_toolbar_title);

        bottomAppBar.setOnMenuItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        fab.setOnClickListener(this);
        titleToolbar.setText(getTitleToolbar());

        updateBottomAppBar();
        updateUI();
        return view;
    }

    private void updateBottomAppBar() {
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        bottomAppBar.replaceMenu(R.menu.list_bottom_app_bar_menu);
        bottomAppBar.performShow();
        fab.setImageResource(R.drawable.ic_add);
    }

    private void updateUI() {
        List<Book> books = getSortedBooks(getBooks());

        if (books.size() == 0) listEmptyView.setVisibility(View.VISIBLE);
        else listEmptyView.setVisibility(View.INVISIBLE);

        if (bookAdapter == null) {
            bookAdapter = new BookAdapter(books, getFragmentManager());
        } else {
            bookAdapter.setBooks(books);
            bookAdapter.notifyDataSetChanged();
        }
        bookAdapter.setRootLayout(rootLayout);
        recyclerView.setAdapter(bookAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new SwipeToDeleteCallback(bookAdapter, getActivity()));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private List<Book> getSortedBooks(List<Book> books) {
        int sortMode = InitApplication.getInstance().getSortMode();

        switch (sortMode) {
            case InitApplication.SORT_MODE_TITLE:
                return BookLab.get(getActivity()).getBooksSortedByTitle(books);
            case InitApplication.SORT_MODE_AUTHOR:
                return BookLab.get(getActivity()).getBooksSortedByAuthor(books);
            case InitApplication.SORT_MODE_DATE:
                return BookLab.get(getActivity()).getBooksSortedByDate(books);
        }
        return books;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                goToEditFragment();
                break;
        }
    }

    private void goToEditFragment() {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                EditFragment.newInstance(getStringTag())).addToBackStack(EditFragment.TAG).commit();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        fab.hide();
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        fab.show();
                        return true;
                    }
                });

                SearchView searchView = (SearchView) item.getActionView();
                searchView.setQueryHint(getString(R.string.hint_search_view));
                searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        bookAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
                break;
            case R.id.filter_list:
                showPopupMenu();
                break;
        }
        return true;
    }

    private void showPopupMenu() {
        View itemMenuView = getActivity().findViewById(R.id.filter_list);
        PopupMenu popupMenu = new PopupMenu(getActivity(), itemMenuView);
        popupMenu.inflate(R.menu.popup_filter_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                List<Book> books = getBooks();
                int sortMode = 1;
                switch (item.getItemId()) {
                    case R.id.filter_title_item:
                        BookLab.get(getActivity()).getBooksSortedByTitle(books);
                        sortMode = InitApplication.SORT_MODE_TITLE;
                        break;
                    case R.id.filter_author_item:
                        BookLab.get(getActivity()).getBooksSortedByAuthor(books);
                        sortMode = InitApplication.SORT_MODE_AUTHOR;
                        break;
                    case R.id.filter_date_item:
                        BookLab.get(getActivity()).getBooksSortedByDate(books);
                        sortMode = InitApplication.SORT_MODE_DATE;
                        break;
                }
                InitApplication.getInstance().setSortMode(sortMode);
                bookAdapter.setBooks(books);
                bookAdapter.notifyDataSetChanged();
                return true;
            }
        });
        popupMenu.show();
    }
}
