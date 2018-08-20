package com.celestialapps.imagesearch.network.api;


import com.celestialapps.imagesearch.network.model.response.suggestion.Toplevel;


import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleSuggestionsApi {


    @GET("complete/search?output=toolbar")
    Observable<Response<Toplevel>> getSuggestions(@Query("q") String query);
}
