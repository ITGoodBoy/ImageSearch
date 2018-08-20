package com.celestialapps.imagesearch.dagger.modules;



import com.celestialapps.imagesearch.network.api.GoogleSuggestionsApi;
import com.celestialapps.imagesearch.network.api.ImageSearchApi;


import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module(includes = RetrofitModule.class)
public class ApiModule {



    @Provides
    @Singleton
    public ImageSearchApi provideImageSearchApi(@Named("GsonRetrofit") Retrofit retrofit) {
        return retrofit.create(ImageSearchApi.class);
    }

    @Provides
    @Singleton
    public GoogleSuggestionsApi provideGoogleSuggestionsApi(@Named("SimpleXmlRetrofit") Retrofit retrofit) {
       return retrofit.create(GoogleSuggestionsApi.class);
    }


}
