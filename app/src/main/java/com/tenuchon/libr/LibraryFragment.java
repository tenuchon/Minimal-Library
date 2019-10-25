package com.tenuchon.libr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class LibraryFragment extends DefaultFragment implements View.OnClickListener {
    private static final String TAG = "LibraryFragment";
    private BottomAppBar mBottomAppBar;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private BookAdapter mBookAdapter;
    private Toolbar mToolbar;

    public static Fragment newInstance() {
        return new LibraryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        mRecyclerView = view.findViewById(R.id.library_recycler_view);
        mToolbar = view.findViewById(R.id.toolbar);
        mBottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        mFab = getActivity().findViewById(R.id.fab);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mBottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        mBottomAppBar.replaceMenu(R.menu.list_bottom_app_bar_menu);
        mFab.setImageResource(R.drawable.ic_add);
        mFab.setOnClickListener(this);

        TextView textViewToolbarTitle = mToolbar.findViewById(R.id.tv_toolbar_title);
        String toolbarTitle = getString(R.string.library);
        textViewToolbarTitle.setText(toolbarTitle);
        updateUI();

        return view;
    }

    private void updateUI() {
        BookLab bookLab = BookLab.get(getActivity());
        List<Book> books = bookLab.getBooks();
        if (mBookAdapter == null) {
            mBookAdapter = new BookAdapter(books,getFragmentManager(),getActivity());
        } else {
            mBookAdapter.setBooks(books);
            mBookAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setAdapter(mBookAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new SwipeToDeleteCallback(mBookAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        EditFragment.newInstance()).addToBackStack(EditFragment.TAG).commit();
                break;
        }
    }

}
