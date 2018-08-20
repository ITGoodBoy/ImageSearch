package com.celestialapps.imagesearch.network.model.response.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @SerializedName("contextLink")
    @Expose
    private String contextLink;
    @SerializedName("height")
    @Expose
    private int height;
    @SerializedName("width")
    @Expose
    private int width;
    @SerializedName("byteSize")
    @Expose
    private int byteSize;
    @SerializedName("thumbnailLink")
    @Expose
    private String thumbnailLink;
    @SerializedName("thumbnailHeight")
    @Expose
    private int thumbnailHeight;
    @SerializedName("thumbnailWidth")
    @Expose
    private int thumbnailWidth;
}
