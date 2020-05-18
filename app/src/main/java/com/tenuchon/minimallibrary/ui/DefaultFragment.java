package com.tenuchon.minimallibrary.ui;

import androidx.fragment.app.Fragment;

public abstract class DefaultFragment extends Fragment {

    public void onBackClick(){
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }
    }
}
