package com.tenuchon.libr;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends SingleFragmentActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    public BottomAppBar mBottomAppBar;

    protected Fragment createFragment() {
        return LibraryFragment.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBottomAppBar = findViewById(R.id.bottom_app_bar);

        mBottomAppBar.replaceMenu(R.menu.book_bottom_app_bar_menu);
        mBottomAppBar.setNavigationOnClickListener(this);
        mBottomAppBar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_bottom_app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
    }


    @Override
    public void onClick(View v) {
        BottomNavDrawerFragment fragment = new BottomNavDrawerFragment();
        fragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(getApplication(), "this is Toast", Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
