package com.celestialapps.imagesearch.realm;

import com.celestialapps.imagesearch.realm.model.RealmImage;

import io.realm.Realm;
import io.realm.RealmModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RealmHelper {

    private Realm mRealm;

    public long getNextId(Class<? extends RealmModel> clazz) {
        Number number = mRealm.where(clazz).max("id");
        return number == null ? 1 : number.longValue() + 1;
    }

    public boolean isFieldAlreadyHasData(Class<? extends RealmModel> clazz, String field, long id) {
        return mRealm.where(RealmImage.class).equalTo(field, id).findFirst() != null;
    }

    public boolean isFieldAlreadyHasData(Class<? extends RealmModel> clazz, String field, String fieldData) {
        return mRealm.where(RealmImage.class).equalTo(field, fieldData).findAll().size() > 0;
    }
}
