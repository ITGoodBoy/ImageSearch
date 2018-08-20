package com.celestialapps.imagesearch.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiKey extends RealmObject {

    @PrimaryKey
    private String apiKey;
    private boolean isActive;
}
