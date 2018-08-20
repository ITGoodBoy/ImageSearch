package com.celestialapps.imagesearch.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.celestialapps.imagesearch.R;

import java.util.List;

public class SuggestionAdapter extends CursorAdapter {

    private SearchView mSearchView;
    private List<String> mItems;
    private AppCompatTextView mAcTvSuggestion;
    private AppCompatImageView mAcImvCompleteText;
    private LayoutInflater mLayoutInflater;

    public SuggestionAdapter(SearchView searchView, Context context, Cursor cursor, List<String> items) {
        super(context, cursor, false);
        mSearchView = searchView;
        mLayoutInflater = LayoutInflater.from(context);
        mItems = items;
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.item_suggestion_text, parent, false);

        mAcTvSuggestion = view.findViewById(R.id.ac_tv_suggestion);
        mAcImvCompleteText = view.findViewById(R.id.ac_imv_complete_text);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String text = mItems.get(cursor.getPosition());

        mAcTvSuggestion.setText(text);

        mAcTvSuggestion.setOnClickListener(v -> mSearchView.setQuery(text, true));
        mAcImvCompleteText.setOnClickListener(v -> mSearchView.setQuery(text, false));
    }
}
