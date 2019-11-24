package com.tenuchon.minimallibrary.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.tenuchon.minimallibrary.R;
import com.tenuchon.minimallibrary.ui.DefaultFragment;
import com.tenuchon.minimallibrary.utils.InitApplication;

public class SettingsFragment extends DefaultFragment {
    public static final String TAG = "SettingsFragment";

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private BottomAppBar bottomAppBar;
    private SwitchMaterial darkModeSwitch;
    private TextView titleToolbar;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        fab = getActivity().findViewById(R.id.fab);
        bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        darkModeSwitch = view.findViewById(R.id.sw_dark_mode);
        titleToolbar = toolbar.findViewById(R.id.tv_toolbar_title);

        if (InitApplication.getInstance().isNightModeEnabled()) darkModeSwitch.setChecked(true);
        else darkModeSwitch.setChecked(false);

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                InitApplication.getInstance().setIsNightModeEnabled(isChecked);
            }
        });

        titleToolbar.setText(getString(R.string.settings));
        updateBottomAppBar();
        return view;
    }

    private void updateBottomAppBar() {
        bottomAppBar.performShow();
        bottomAppBar.replaceMenu(R.menu.empty_bottom_app_bar_menu);
        fab.hide();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fab.show();
    }
}
