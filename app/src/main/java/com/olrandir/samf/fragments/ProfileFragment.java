package com.olrandir.samf.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.olrandir.samf.R;
import com.olrandir.samf.models.SamfPerson;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    EditText mNameField, mLocationField;
    Button mSaveButton;

    SamfPerson mPerson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View screen = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get field and button handles
        mNameField      =   (EditText)  screen.findViewById(R.id.profile_field_name);
        mLocationField  =   (EditText)  screen.findViewById(R.id.profile_field_location);
        mSaveButton     =   (Button)    screen.findViewById(R.id.profile_button_save);

        return screen;
    }

    @Override
    public void onFABClick() {
        showMessage(getString(R.string.profile_error_alreadyInProfile));
    }

    @Override
    public void onStart(){
        super.onStart();

        // Attach onClickListener to buttons
        mSaveButton.setOnClickListener(this);

        // Initialize our data
        mPerson = ((ProfileActions)getActivity()).getStoredPerson();

        // Fill in fields with initial data, if available
        if( mPerson!=null ){
            mNameField.setText(mPerson.getName());
            mLocationField.setText(mPerson.getLocation());
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.profile_button_save){
            // First, make sure we actually have values to store
            if(TextUtils.isEmpty(mNameField.getText()) ){
                showMessage(getString(R.string.profile_error_nameEmpty));
                return;
            }
            if(TextUtils.isEmpty(mLocationField.getText()) ){
                showMessage(getString(R.string.profile_error_locationEmpty));
                return;
            }

            // Get new data
            String name     = mNameField.getText().toString();
            String location = mLocationField.getText().toString();

            SamfPerson person = new SamfPerson(name, location);

            ((ProfileActions)getActivity()).setStoredPerson(person);

            showMessage(getString(R.string.profile_success_saved));

            ((ProfileActions)getActivity()).goToHome();
        }
    }

    public interface ProfileActions {
        public SamfPerson getStoredPerson();
        public void setStoredPerson(SamfPerson person);
        public void goToHome();
    }
}
