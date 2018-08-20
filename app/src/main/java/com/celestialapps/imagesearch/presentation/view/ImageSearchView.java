package com.celestialapps.imagesearch.presentation.view;

import android.view.Menu;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.celestialapps.imagesearch.network.model.response.search.Item;
import com.celestialapps.imagesearch.network.model.response.suggestion.Suggestion;
import com.celestialapps.imagesearch.network.model.response.suggestion.Toplevel;

import java.util.List;

public interface ImageSearchView extends MvpView {

    void searchImagesSuccess(List<Item> images, String query);

    void searchImagesNextPageSuccess(List<Item> images, String query);

    void getSuggestionsSuccess(List<String> suggestions);

    void saveMenuInstanceStateSuccess(Menu menu);
}
