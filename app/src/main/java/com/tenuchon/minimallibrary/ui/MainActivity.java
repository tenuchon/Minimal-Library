package com.tenuchon.minimallibrary.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.tenuchon.minimallibrary.R;
import com.tenuchon.minimallibrary.ui.library.LibraryFragment;
import com.tenuchon.minimallibrary.utils.InitApplication;

public class MainActivity extends SingleFragmentActivity implements View.OnClickListener {
    public BottomAppBar bottomAppBar;

    protected Fragment createFragment() {
        return LibraryFragment.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (InitApplication.getInstance().isNightModeEnabled())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        bottomAppBar = findViewById(R.id.bottom_app_bar);
        bottomAppBar.replaceMenu(R.menu.book_bottom_app_bar_menu);
        bottomAppBar.setNavigationOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_bottom_app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onClick(View v) {
        BottomNavDrawerFragment fragment = BottomNavDrawerFragment.newInstance();
        fragment.show(getSupportFragmentManager(), BottomNavDrawerFragment.TAG);
    }
}
