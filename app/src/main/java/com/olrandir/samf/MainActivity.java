package com.olrandir.samf;

import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.olrandir.samf.fragments.BaseFragment;
import com.olrandir.samf.fragments.HomeFragment;
import com.olrandir.samf.fragments.ProfileFragment;
import com.olrandir.samf.models.SamfPerson;

public class MainActivity
        extends AppCompatActivity
        implements ProfileFragment.ProfileActions, HomeFragment.HomeActions, View.OnClickListener {
    BaseFragment currentScreen;

    FloatingActionButton editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create first screen
        if( currentScreen==null ){
            currentScreen = new HomeFragment();
        }

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.mainactivity_content, currentScreen)
            .commit();
        onTransition(null, currentScreen);

        // Add a backstack listener so we get the handle for the new screen when user presses back
        getSupportFragmentManager().addOnBackStackChangedListener(
            new android.support.v4.app.FragmentManager.OnBackStackChangedListener() {
                public void onBackStackChanged() {
                    currentScreen = (BaseFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.mainactivity_content);
                }
            }
        );

        // Initialize views belonging to the main activity
        editButton  = (FloatingActionButton)    findViewById(R.id.activity_button_edit);

        editButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if( currentScreen != null ){
            currentScreen.onFABClick();
        }
    }

    @Override
    public SamfPerson getStoredPerson() {
        return getUser();
    }

    @Override
    public void setStoredPerson(SamfPerson person) {
        storeUser(person);
    }

    @Override
    public void goToHome() {
        transitionTo(new HomeFragment());
    }

    @Override
    public void goToProfile() {
        transitionTo(new ProfileFragment());
    }

    /*
     * Interactions
     */
    public int transitionTo(BaseFragment newFragment){
        // Setup animation
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(
                R.anim.slide_in_right, R.anim.slide_out_left, //entering fragment animation, exiting fragment animation
                R.anim.slide_in_left, R.anim.slide_out_right) //reverse for on back pressed
            .replace(R.id.mainactivity_content, newFragment, newFragment.getClass().getSimpleName())
            .addToBackStack(newFragment.getClass().getSimpleName());

        // Perform animation
        int id = ft.commit();

        onTransition(currentScreen, newFragment);

        return id;
    }

    protected void onTransition(BaseFragment oldFragment, BaseFragment newFragment){
        currentScreen = newFragment;
        //TODO: e.g. analytics tracking
    }

    /*
     * Data handling functions; these should be extracted to a separate class, for clearer
     * separation of concerns. The important thing to note is that all data is handled through the
     * activity, not directly from the fragment.
     */
    private final static String DATA_STORE_NAME = "MyDataStore";
    private final static String DATA_KEY_USER = "user";

    private static Gson gson = new GsonBuilder().create();

    /*
     * Higher level functions, to provide an API to the business layer. Talks in models.
     */
    protected SamfPerson getUser(){
        String encodedPerson = getStoredData(DATA_KEY_USER);

        SamfPerson person = gson.fromJson(encodedPerson, SamfPerson.class);

        return person;
    }

    protected void storeUser(SamfPerson person){
        String encodedUser = gson.toJson(person,SamfPerson.class);

        storeData(DATA_KEY_USER, encodedUser);
    }

    /*
     * Lower level functions to interact with the storage layer. Talks in whatever structure the
     * storage layer uses (in this case, strings).
     */
    private String getStoredData(String key){
        SharedPreferences settings = getSharedPreferences(DATA_STORE_NAME, 0);
        String result = settings.getString(key, null);
        return result;
    }

    private void storeData(String key, String value){
        SharedPreferences settings = getSharedPreferences(DATA_STORE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
