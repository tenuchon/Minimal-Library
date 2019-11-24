package com.tenuchon.minimallibrary.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tenuchon.minimallibrary.BuildConfig;
import com.tenuchon.minimallibrary.R;
import com.tenuchon.minimallibrary.ui.DefaultFragment;

public class AboutFragment extends DefaultFragment {
    public static final String TAG = "AboutFragment";

    private String appVersion;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private BottomAppBar bottomAppBar;
    private TextView appVersionTextView, titleToolbar;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appVersion = BuildConfig.VERSION_NAME;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        fab = getActivity().findViewById(R.id.fab);
        bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        appVersionTextView = view.findViewById(R.id.tv_version);
        titleToolbar = toolbar.findViewById(R.id.tv_toolbar_title);

        appVersionTextView.setText(String.format(getResources().getString(R.string.app_version), appVersion));
        titleToolbar.setText(getString(R.string.about));
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
