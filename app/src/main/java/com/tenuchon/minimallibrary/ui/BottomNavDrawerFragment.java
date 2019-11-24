package com.tenuchon.minimallibrary.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.tenuchon.minimallibrary.R;
import com.tenuchon.minimallibrary.ui.about.AboutFragment;
import com.tenuchon.minimallibrary.ui.favorite.FavoriteFragment;
import com.tenuchon.minimallibrary.ui.library.LibraryFragment;
import com.tenuchon.minimallibrary.ui.settings.SettingsFragment;
import com.tenuchon.minimallibrary.ui.wishlist.WishListFragment;

public class BottomNavDrawerFragment extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "BottomNavDrawerFragment";

    public static BottomNavDrawerFragment newInstance() {
        return new BottomNavDrawerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bottomsheet, container, false);
        NavigationView navigationView = view.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        return view;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.library:
                transactionFragment(LibraryFragment.newInstance(), LibraryFragment.TAG);
                break;
            case R.id.favorite:
                transactionFragment(FavoriteFragment.newInstance(), FavoriteFragment.TAG);
                break;
            case R.id.wish_list:
                transactionFragment(WishListFragment.newInstance(), WishListFragment.TAG);
                break;
            case R.id.settings:
                transactionFragment(SettingsFragment.newInstance(), SettingsFragment.TAG);
                break;
            case R.id.about:
                transactionFragment(AboutFragment.newInstance(), AboutFragment.TAG);
                break;
        }
        dismiss();
        return true;
    }

    private void transactionFragment(Fragment fragment, String tag) {
        FragmentManager fm = getFragmentManager();
        if (fm != null) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(tag)
                    .commit();
        }
    }
}
