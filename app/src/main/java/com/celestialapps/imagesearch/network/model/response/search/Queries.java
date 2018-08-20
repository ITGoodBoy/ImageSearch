package com.celestialapps.imagesearch.network.model.response.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Queries {

    @SerializedName("request")
    @Expose
    private List<Request> requests;
    @SerializedName("nextPage")
    @Expose
    private List<NextPage> nextPages;
}
