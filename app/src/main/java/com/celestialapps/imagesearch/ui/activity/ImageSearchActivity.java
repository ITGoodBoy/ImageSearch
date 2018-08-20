package com.celestialapps.imagesearch.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.celestialapps.imagesearch.R;
import com.celestialapps.imagesearch.network.model.response.search.Item;
import com.celestialapps.imagesearch.presentation.presenter.GlidePresenter;
import com.celestialapps.imagesearch.presentation.presenter.ImageSearchPresenter;
import com.celestialapps.imagesearch.presentation.view.GlideView;
import com.celestialapps.imagesearch.presentation.view.ImageSearchView;
import com.celestialapps.imagesearch.ui.adapter.ImageSearchAdapter;
import com.celestialapps.imagesearch.ui.adapter.SuggestionAdapter;
import com.paginate.Paginate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ImageSearchActivity extends MvpAppCompatActivity
        implements ImageSearchView, Paginate.Callbacks, GlideView {

    @InjectPresenter
    ImageSearchPresenter mImageSearchPresenter;
    @InjectPresenter
    GlidePresenter mGlidePresenter;

    @BindView(R.id.rv_search_images)
    RecyclerView mRvSearchImages;

    private final int PERMISSIONS_REQUEST_CODE = 6234;

    private ImageSearchAdapter mImageSearchAdapter;
    private SearchView mSearchView;
    private Paginate mPaginate;

    private String mCurrentQuery = "";
    private boolean mIsLoading;
    private int mPage = 1;
    private int mPageSize = 10;
    private int mMaxPage = 91;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        ButterKnife.bind(this);

        mRvSearchImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvSearchImages.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {

                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            finish();
                        }
                    }
                } else finish();

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        mImageSearchPresenter.saveMenuInstanceState(menu);

        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mCurrentQuery.equalsIgnoreCase(query)) {
                    mImageSearchPresenter.searchImagesNextPage(query, mPage, mPageSize);
                } else {
                    mPage = 1;

                    if (mPaginate != null) {
                        mPaginate.unbind();
                        mPaginate = null;
                    }

                    mImageSearchPresenter.searchImages(query, mPage, mPageSize);
                }

                mSearchView.setQuery("", false);
                mSearchView.setSuggestionsAdapter(null);
                mSearchView.setQuery(query, false);

                hideKeyboard();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mImageSearchPresenter.getSuggestions(query);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void searchImagesSuccess(List<Item> images, String query) {
        mImageSearchAdapter = new ImageSearchAdapter(this, images, mGlidePresenter, query);
        mRvSearchImages.setAdapter(mImageSearchAdapter);
        setupPagination();
        mCurrentQuery = query;
    }


    private void setupPagination() {
        mPaginate = Paginate
                .with(mRvSearchImages, this)
                .setLoadingTriggerThreshold(5)
                .build();
    }

    @Override
    public void searchImagesNextPageSuccess(List<Item> images, String query) {
        mIsLoading = false;
        mImageSearchAdapter.addItems(images);
        mCurrentQuery = query;
    }

    @Override
    public void getSuggestionsSuccess(List<String> suggestions) {

        Object[] temp = new Object[] { 0, "default" };
        MatrixCursor matrixCursor = new MatrixCursor(new String[] { "_id", "text" });

        for (int i = 0; i < suggestions.size(); i++) {
            temp[0] = i;
            temp[1] = suggestions.get(i);
            matrixCursor.addRow(temp);
        }

        mSearchView.setSuggestionsAdapter(new SuggestionAdapter(mSearchView, this, matrixCursor, suggestions));
    }

    @Override
    public void saveMenuInstanceStateSuccess(Menu menu) {
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
    }

    @Override
    public void onLoadMore() {
        mIsLoading = true;
        mImageSearchPresenter.searchImagesNextPage(mCurrentQuery, mPage+=mPageSize, mPageSize);
    }

    @Override
    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return mPage == mMaxPage;
    }
}
