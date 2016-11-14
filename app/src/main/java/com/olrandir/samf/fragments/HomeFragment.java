package com.olrandir.samf.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olrandir.samf.R;
import com.olrandir.samf.models.SamfPerson;

/**
 * Created by olrandir on 13/11/2016.
 */

public class HomeFragment extends BaseFragment {

    TextView mGreeting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View screen = inflater.inflate(R.layout.fragment_home, container, false);

        // Get view handles
        mGreeting   =   (TextView)  screen.findViewById(R.id.home_text_greeting);

        return screen;
    }

    @Override
    public void onFABClick() {
        ((HomeActions)getActivity()).goToProfile();
    }

    @Override
    public void onStart(){
        super.onStart();

        // Initialize data
        SamfPerson user = ((HomeActions)getActivity()).getStoredPerson();
        String name = getString(R.string.home_text_greeting_nameUnknown);

        // Fill in fields with initial data, if available
        if( user != null  && !TextUtils.isEmpty(user.getName()) ){
            name = user.getName();
        }

        mGreeting.setText(
            getString(R.string.home_text_greeting).replace("$1", name)
            );
    }

    public interface HomeActions {
        public SamfPerson getStoredPerson();
        public void goToProfile();
    }
}
