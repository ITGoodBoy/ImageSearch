package com.celestialapps.imagesearch.network.api;


import com.celestialapps.imagesearch.network.model.response.search.SearchResult;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ImageSearchApi {

    @GET("customsearch/v1?searchType=image")
    Observable<Response<SearchResult>> searchImages(@Query("key") String key,
                                                    @Query("q") String query,
                                                    @Query("start") int startIndex,
                                                    @Query("num") int imageCount);
}
