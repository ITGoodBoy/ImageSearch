package com.celestialapps.imagesearch.network.model.response.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Context {

    @SerializedName("title")
    @Expose
    private String title;

}
