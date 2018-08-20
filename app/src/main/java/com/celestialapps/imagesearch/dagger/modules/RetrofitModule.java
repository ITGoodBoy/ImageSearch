package com.celestialapps.imagesearch.dagger.modules;

import android.content.Context;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.io.File;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


@Module
public class RetrofitModule {

    private final String BASE_URL = "https://www.googleapis.com/";
    private final String GOOGLE_SUGGESTIONS_BASE_URL = "http://google.com/";

   // private final Pair<String, String> keyApiPair = new Pair<>("key", "AIzaSyAyxSrkv-Nj3z4ctZqasLNZEXIf3usVcvE");
    private final Pair<String, String> searchEngineIdPair = new Pair<>("cx", "001417009700714159836:sbvn6n-wvwi");


    @Provides
    @Singleton
    @Named("GsonRetrofit")
    public Retrofit provideGsonRetrofit(@Named("GsonConverterFactory") Converter.Factory gsonConvectorFactory,
                                        @Named("GsonOkHttpClient") OkHttpClient okHttpClient,
                                        CallAdapter.Factory callAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(gsonConvectorFactory)
                .build();
    }

    @Provides
    @Singleton
    @Named("SimpleXmlRetrofit")
    public Retrofit provideSimpleXmlRetrofit(@Named("SimpleXmlConverterFactory") Converter.Factory simpleXmlConverterFactory,
                                             @Named("SimpleXmlOkHttpClient") OkHttpClient okHttpClient,
                                             CallAdapter.Factory callAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(GOOGLE_SUGGESTIONS_BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(simpleXmlConverterFactory)
                .build();
    }


    @Provides
    @Singleton
    public CallAdapter.Factory provideCallAdapterFactory() {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
    }

    @Provides
    @Singleton
    @Named("GsonConverterFactory")
    public Converter.Factory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    @Named("SimpleXmlConverterFactory")
    public Converter.Factory provideSimpleXmlConverterFactory() {
        return SimpleXmlConverterFactory.createNonStrict(new Persister(new AnnotationStrategy()));
    }

//    @Provides
//    @Singleton
//    @Named("ApiKeyInterceptor")
//    public Interceptor provideApiKeyInterceptor() {
//        return chain -> {
//            Request request = chain.request();
//            HttpUrl url = request.url().newBuilder().addQueryParameter(keyApiPair.first, keyApiPair.second).build();
//            request = request.newBuilder().url(url).build();
//            return chain.proceed(request);
//        };
//    }

    @Provides
    @Singleton
    @Named("SearchEngineIdInterceptor")
    public Interceptor provideSearchEngineIdInterceptor() {
        return chain -> {
            Request request = chain.request();
            HttpUrl url = request.url().newBuilder().addQueryParameter(searchEngineIdPair.first, searchEngineIdPair.second).build();
            request = request.newBuilder().url(url).build();
            return chain.proceed(request);
        };
    }

    @Provides
    @Singleton
    @Named("HttpLoggingInterceptor")
    public Interceptor provideHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Singleton
    @Named("CacheControlInterceptor")
    public Interceptor provideCacheControlInterceptor(Context context) {
        return chain -> {
            Response originalResponse = chain.proceed(chain.request());
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        };
    }

    @Provides
    @Singleton
    @Named("GsonOkHttpClient")
    public OkHttpClient provideGsonOkHttpClient(@Named("HttpLoggingInterceptor") Interceptor httpLoggingInterceptor,
                                    //            @Named("ApiKeyInterceptor") Interceptor apiKeyInterceptor,
                                                @Named("SearchEngineIdInterceptor") Interceptor searchEngineIdInterceptor,
                                                @Named("CacheControlInterceptor") Interceptor cacheControlInterceptor,
                                                Cache cache) {
        return new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
         //       .addInterceptor(apiKeyInterceptor)
                .addInterceptor(searchEngineIdInterceptor)
                .addNetworkInterceptor(cacheControlInterceptor)
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    @Named("SimpleXmlOkHttpClient")
    public OkHttpClient provideSimpleXmlOkHttpClient(@Named("HttpLoggingInterceptor") Interceptor httpLoggingInterceptor,
                                                     @Named("CacheControlInterceptor") Interceptor cacheControlInterceptor,
                                                     Cache cache) {
        return new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(cacheControlInterceptor)
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    public Cache provideCache(File file) {
        return new Cache(file, 10 * 1024 * 1024); // 10 MiB
    }

    @Provides
    @Singleton
    public File provideHttpCacheDirectory(Context context) {
        return new File(context.getCacheDir(), "responses");
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

}
