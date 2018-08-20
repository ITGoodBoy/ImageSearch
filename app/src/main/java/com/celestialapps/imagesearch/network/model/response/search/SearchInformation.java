package com.celestialapps.imagesearch.network.model.response.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchInformation {

    @SerializedName("searchTime")
    @Expose
    private float searchTime;
    @SerializedName("formattedSearchTime")
    @Expose
    private String formattedSearchTime;
    @SerializedName("totalResults")
    @Expose
    private String totalResults;
    @SerializedName("formattedTotalResults")
    @Expose
    private String formattedTotalResults;
}
