package com.creativelair.handyphone;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HishamAhmed on 19-Mar-17.
 */

public class Categorized_Contacts extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.contacts_items, container, false);
        return rootView;
    }
}
