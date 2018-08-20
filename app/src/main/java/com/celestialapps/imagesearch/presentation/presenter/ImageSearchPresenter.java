package com.celestialapps.imagesearch.presentation.presenter;

import android.view.Menu;

import com.arellomobile.mvp.InjectViewState;
import com.celestialapps.imagesearch.App;
import com.celestialapps.imagesearch.help.Utils;
import com.celestialapps.imagesearch.network.api.GoogleSuggestionsApi;
import com.celestialapps.imagesearch.network.api.ImageSearchApi;
import com.celestialapps.imagesearch.network.model.response.search.SearchResult;
import com.celestialapps.imagesearch.network.model.response.suggestion.CompleteSuggestion;
import com.celestialapps.imagesearch.network.model.response.suggestion.Suggestion;
import com.celestialapps.imagesearch.network.model.response.suggestion.Toplevel;
import com.celestialapps.imagesearch.presentation.view.ImageSearchView;
import com.celestialapps.imagesearch.realm.model.ApiKey;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.Realm;
import retrofit2.Response;

import static com.celestialapps.imagesearch.constant.HttpResponseCode.FORBIDDEN;


@InjectViewState
public class ImageSearchPresenter extends BasePresenter<ImageSearchView> {

    @Inject
    ImageSearchApi mImageSearchApi;
    @Inject
    GoogleSuggestionsApi mGoogleSuggestionsApi;
    @Inject
    Realm mRealm;

    private ApiKey mApiKey;

    public ImageSearchPresenter() {
        App.getAppComponent().inject(this);
        updateActiveApiKey();
    }

    public void saveMenuInstanceState(Menu menu) {
        getViewState().saveMenuInstanceStateSuccess(menu);
    }

    public void getSuggestions(String query) {
        Disposable disposable = mGoogleSuggestionsApi
                .getSuggestions(query)
                .compose(Utils.applySchedulers())
                .map(toplevelResponse -> {
                    List<String> suggestions = new ArrayList<>();

                    List<CompleteSuggestion> completeSuggestions = toplevelResponse.body().getCompleteSuggestions();

                    for (CompleteSuggestion completeSuggestion : completeSuggestions) {
                        suggestions.add(completeSuggestion.getSuggestion().getData());
                    }

                    return suggestions;
                })
                .subscribe(getViewState()::getSuggestionsSuccess, Throwable::printStackTrace);

        disposeOnDestroy(disposable);
    }


    public void searchImages(String query, int page, int imageCount) {
        Disposable disposable = mImageSearchApi
                .searchImages(mApiKey.getApiKey(), query, page, imageCount)
                .compose(Utils.applySchedulers())
                .subscribe(searchResultResponse -> {
                    if (searchResultResponse.code() == FORBIDDEN) {
                        inactiveApiKey();
                        updateActiveApiKey();
                        searchImages(query, page, imageCount);
                    } else {
                        if (searchResultResponse.body() != null) {
                            SearchResult searchResult = searchResultResponse.body();
                            if (searchResult != null) {
                                getViewState().searchImagesSuccess(searchResult.getItems(), query);
                            }
                        }
                    }
                }, Throwable::printStackTrace);


        disposeOnDestroy(disposable);
    }

    public void searchImagesNextPage(String query, int page, int imageCount) {
        Disposable disposable = mImageSearchApi
                .searchImages(mApiKey.getApiKey(), query, page, imageCount)
                .compose(Utils.applySchedulers())
                .subscribe(searchResultResponse -> {
                    if (searchResultResponse.code() == FORBIDDEN) {
                        inactiveApiKey();
                        updateActiveApiKey();
                        searchImagesNextPage(query, page, imageCount);
                    }
                    else {
                        SearchResult searchResult = searchResultResponse.body();
                        if (searchResult != null) {
                            getViewState().searchImagesNextPageSuccess(searchResult.getItems(), query);
                        }
                    }
                }, Throwable::printStackTrace);


        disposeOnDestroy(disposable);
    }

    private void updateActiveApiKey() {
        mApiKey = mRealm.where(ApiKey.class).equalTo("isActive", true).findFirst();
    }

    private void inactiveApiKey() {
        mRealm.executeTransaction(realm -> mApiKey.setActive(false));
    }

}
