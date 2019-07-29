package ru.kazantsev.zimadtest;

import android.app.Application;

import com.squareup.picasso.Picasso;

import ru.kazantsev.zimadtest.dagger.AppComponent;
import ru.kazantsev.zimadtest.dagger.DaggerAppComponent;
import ru.kazantsev.zimadtest.dagger.ContextModule;

public class App extends Application {

    private static App singleton;

    public static App getInstance() {
        return singleton;
    }

    private static AppComponent component;


    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        component = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
        Picasso.setSingletonInstance(new Picasso.Builder(this).build());
    }

    public AppComponent getComponent() {
        return component;
    }
}
