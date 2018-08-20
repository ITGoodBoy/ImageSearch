package com.celestialapps.imagesearch.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RealmImage extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private String group;
    private long createAt;
    private String link;
    private String localLink;
    private double longitude;
    private double latitude;

}
