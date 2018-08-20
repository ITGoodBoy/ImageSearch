package com.celestialapps.imagesearch.network.model.response.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "link")
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("htmlTitle")
    @Expose
    private String htmlTitle;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("displayLink")
    @Expose
    private String displayLink;
    @SerializedName("snippet")
    @Expose
    private String snippet;
    @SerializedName("htmlSnippet")
    @Expose
    private String htmlSnippet;
    @SerializedName("mime")
    @Expose
    private String mime;
    @SerializedName("image")
    @Expose
    private Image image;
}
