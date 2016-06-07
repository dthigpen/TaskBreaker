package com.davidthigs.david.taskbreaker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;

public class AppPreferences extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(MainActivity.PREF_DARK_THEME,false)){
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        setTitle("Preferences");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PreferenceFragment settingsFragment = new SettingsFragment();
        fragmentTransaction.add(android.R.id.content,settingsFragment,"SETTINGS_FRAGMENT");
        fragmentTransaction.commit();


    }


    @Override
    public void onBackPressed(){

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        private void toggleTheme(boolean isDark) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
            editor.putBoolean(MainActivity.PREF_DARK_THEME, isDark);
            editor.apply();

            Intent intent = getActivity().getIntent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().finish();
            startActivity(intent);

        }


        @Override
        public void onResume(){
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause(){
            super.onPause();
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
            Log.d("Preferences",key.toString());
            if(key.equals(MainActivity.PREF_DARK_THEME)){

                Preference preference = findPreference(key);
                //preference.setSummary(sharedPreferences.getString(key,""));
                toggleTheme(sharedPreferences.getBoolean(MainActivity.PREF_DARK_THEME,false));
                Log.d("Preferences","Theme color");

            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.app_preferences);
        }

    }
}
