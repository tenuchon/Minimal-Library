package com.tenuchon.libr;

import androidx.fragment.app.Fragment;

public abstract class DefaultFragment extends Fragment {
    public void onBackClick(){
        getFragmentManager().popBackStack();
    }
}
