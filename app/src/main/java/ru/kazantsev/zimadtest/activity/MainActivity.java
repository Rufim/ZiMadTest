package ru.kazantsev.zimadtest.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ru.kazantsev.zimadtest.R;
import ru.kazantsev.zimadtest.fragment.PetTabsFragment;


public class MainActivity extends AppCompatActivity {

    public static final String LAST_FRAGMENT_TAG = "last_fragment_tag";
    public static final String CONFIG_CHANGE = "config_change";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment lastFragment =  getLastFragment(savedInstanceState);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (isConfigChange(savedInstanceState) && lastFragment != null) {
            transaction.replace(R.id.container, lastFragment);
        } else {
            transaction.replace(R.id.container, new PetTabsFragment(), PetTabsFragment.class.getSimpleName());
        }
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (getCurrentFragment() != null)
            outState.putString(LAST_FRAGMENT_TAG, getCurrentFragment().getTag());
        outState.putBoolean(CONFIG_CHANGE, true);
        super.onSaveInstanceState(outState);
    }

    public @Nullable Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }

    protected Fragment getLastFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String lastTag = savedInstanceState.getString(LAST_FRAGMENT_TAG);
            if (lastTag != null) {
                FragmentManager manager = getSupportFragmentManager();
                return manager.findFragmentByTag(lastTag);
            }
        }
        return null;
    }

    protected boolean isConfigChange(@Nullable Bundle savedInstanceState) {
        return savedInstanceState != null && savedInstanceState.getBoolean(CONFIG_CHANGE, false);
    }
}
