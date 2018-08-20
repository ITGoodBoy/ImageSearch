package com.celestialapps.imagesearch;

import android.app.Application;

import com.celestialapps.imagesearch.dagger.components.AppComponent;
import com.celestialapps.imagesearch.dagger.components.DaggerAppComponent;
import com.celestialapps.imagesearch.dagger.modules.ContextModule;

import io.realm.Realm;

public class App extends Application {


    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        appComponent = buildAppComponent();
    }

    protected AppComponent buildAppComponent() {
        return DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
    }

}
