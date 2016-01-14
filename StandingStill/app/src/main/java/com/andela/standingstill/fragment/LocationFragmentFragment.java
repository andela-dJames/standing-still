package com.andela.standingstill.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.standingstill.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class LocationFragmentFragment extends Fragment {

    public LocationFragmentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_fragment, container, false);
    }
}
