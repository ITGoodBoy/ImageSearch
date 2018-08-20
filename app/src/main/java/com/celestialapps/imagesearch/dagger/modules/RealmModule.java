package com.celestialapps.imagesearch.dagger.modules;

import com.celestialapps.imagesearch.realm.RealmHelper;
import com.celestialapps.imagesearch.realm.model.ApiKey;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@Module
public class RealmModule {

    @Provides
    @Singleton
    public Realm provideRealm(RealmConfiguration realmConfiguration, List<ApiKey> apiKeys) {
        Realm realm = Realm.getInstance(realmConfiguration);

        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(apiKeys));

        return realm;
    }

    @Provides
    @Singleton
    public RealmConfiguration provideRealmConfiguration() {
        return new RealmConfiguration.Builder().build();
    }

    @Provides
    @Singleton
    public List<ApiKey> provideApiKeys() {
        List<ApiKey> apiKeys = new ArrayList<>();

        apiKeys.add(new ApiKey("AIzaSyBRcX4u5rj0xK9EYnR8jHoMX5FsoTQMzak", true));
        apiKeys.add(new ApiKey("AIzaSyAeJsubd-dOwcamETeCKCp7Kj3dY89PDjE", true));
        apiKeys.add(new ApiKey("AIzaSyDJJoWqZPIj4NlUPW8E1HUP5vjBbhN5WTg", true));
        apiKeys.add(new ApiKey("AIzaSyARY6YLKU18fYdInQW24zGFlgdzYWFcgOw", true));
        apiKeys.add(new ApiKey("AIzaSyAfQp2mDzPzgIzR2kWCD84qXfG25v0wXjc", true));
        apiKeys.add(new ApiKey("AIzaSyA7wjkd20iZADTBloAvyURDEtGnvOdcXDo", true));
        apiKeys.add(new ApiKey("AIzaSyAVXjYfCnBHREj91XM700Lf3Jz0qTSj2yw", true));
        apiKeys.add(new ApiKey("AIzaSyCBxULtsytkvG8kIWh7QA2J6bgSqPvf7Wo", true));
        apiKeys.add(new ApiKey("AIzaSyCtMreLBOuv6-M8DFH91emxymcaNk25NTY", true));
        apiKeys.add(new ApiKey("AIzaSyAJz0IlwYhqEvC7ywSXIGA9c4PleJ60J-Q", true));

        return apiKeys;
    }


    @Provides
    @Singleton
    public RealmHelper provideRealmHelper(Realm realm) {
        return new RealmHelper(realm);
    }
}
