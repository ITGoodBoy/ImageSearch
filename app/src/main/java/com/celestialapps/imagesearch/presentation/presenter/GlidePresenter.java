package com.celestialapps.imagesearch.presentation.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.widget.ImageView;

import com.arellomobile.mvp.InjectViewState;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.celestialapps.imagesearch.App;
import com.celestialapps.imagesearch.glide.GlideApp;
import com.celestialapps.imagesearch.network.model.response.search.Item;
import com.celestialapps.imagesearch.presentation.view.GlideView;
import com.celestialapps.imagesearch.realm.RealmHelper;
import com.celestialapps.imagesearch.realm.model.RealmImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

@InjectViewState
public class GlidePresenter extends BasePresenter<GlideView> {

    @Inject
    Context mContext;
    @Inject
    Realm mRealm;
    @Inject
    RealmHelper mRealmHelper;

    public GlidePresenter() {
        App.getAppComponent().inject(this);
    }

    private List<Address> getLocaleAddresses(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(longitude, latitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addresses;
    }

    @SuppressLint("MissingPermission")
    private Location getLocation() {
        LocationManager lm = (LocationManager) mContext.getSystemService(android.content.Context.LOCATION_SERVICE);

        if (lm != null) {
            return lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        return null;
    }

    public void loadImageAsyncAndSaveImageToInternalStorage(Item image, ImageView imageView, String query) {
        GlideApp
                .with(mContext)
                .asBitmap()
                .load(image.getLink())
                .override(imageView.getWidth(), imageView.getHeight())
                .transform(new CircleCrop())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);

                        long id = mRealmHelper.getNextId(RealmImage.class);
                        String name = query + " # " + id;

                        if (!mRealmHelper.isFieldAlreadyHasData(RealmImage.class, "id", id)
                                && !mRealmHelper.isFieldAlreadyHasData(RealmImage.class, "name", name)
                                && !mRealmHelper.isFieldAlreadyHasData(RealmImage.class, "link", image.getLink())) {

                            RealmImage realmImage = RealmImage.builder()
                                    .id(id)
                                    .name(name)
                                    .group(query)
                                    .link(image.getLink())
                                    .build();

                            mRealm.executeTransaction(realm -> realm.insertOrUpdate(realmImage));

                            parallelSaveImage(resource, realmImage);
                        }
                    }
                });
    }

    public void clearImageView(ImageView imageView) {
        GlideApp.with(mContext).clear(imageView);
    }

    private void parallelSaveImage(Bitmap bitmap, RealmImage realmImage) {
        Disposable disposable = Observable
                .just(getFullLocationInfo())
                .subscribeOn(Schedulers.computation())
                .map(text -> drawTextToBitmap(mContext, bitmap, text))
                .observeOn(Schedulers.io())
                .map(bitmap1 -> saveBitmapToFile("imageSearch/" + realmImage.getGroup(), realmImage.getName() + ".jpg", bitmap1))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(localePath -> {
                        realmImage.setCreateAt(System.currentTimeMillis());
                        realmImage.setLongitude(getLongitude());
                        realmImage.setLatitude(getLatitude());
                        realmImage.setLocalLink(localePath);

                        mRealm.executeTransaction(realm -> realm.insertOrUpdate(realmImage));
                });

        disposeOnDestroy(disposable);
    }

    @NonNull
    private String getFullLocationInfo() {
        return getLocationMainInfo() + getLocationLongitudeAndLatitude(getLongitude(), getLatitude());
    }

    private double getLongitude() {
        Location location = getLocation();
        if (location != null) {
            return location.getLongitude();
        }

        return 0.0;
    }

    private double getLatitude() {
        Location location = getLocation();
        if (location != null) {
            return location.getLatitude();
        }

        return 0.0;
    }

    @NonNull
    private String getLocationLongitudeAndLatitude(double longitude, double latitude) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("longitude : ").append(longitude);
        stringBuilder.append("\n");
        stringBuilder.append("latitude : ").append(latitude);

        return stringBuilder.toString();
    }

    @NonNull
    private String getLocationMainInfo() {
        StringBuilder stringBuilder = new StringBuilder();

        Location location = getLocation();
        if (location != null) {
            List<Address> addresses = getLocaleAddresses(location);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);

                List<String> addressData = new ArrayList<>();

                addressData.add(address.getCountryName());
                addressData.add(address.getLocality());
                addressData.add(address.getThoroughfare());

                for (String data : addressData) {
                    if (data != null && !data.isEmpty() && !data.equalsIgnoreCase("null")) {
                        stringBuilder.append(data);
                        stringBuilder.append("\n");
                    }
                }
            }
        }

        return stringBuilder.toString();
    }


    private Bitmap drawTextToBitmap(android.content.Context mContext, Bitmap bitmap, String text) {
        try {
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;

            android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
            // set default bitmap config if none
            if(bitmapConfig == null) {
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint
            TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.GREEN);
            // text size in pixels
            paint.setTextSize((int) (12 * scale));
            // text shadow
            //    paint.setShadowLayer(1f, 0f, 1f, Color.RED);

            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);

//            int x = (bitmap.getWidth() - bounds.width())/5;
//            int y = (bitmap.getHeight() + bounds.height())/5;

            int x = 100, y = 200;

            for (String line: text.split("\n")) {
                canvas.drawText(line, x, y, paint);
                y += paint.descent() - paint.ascent();
            }

            //  canvas.drawText(text, x * scale, y * scale, paint);

            return bitmap;
        } catch (Exception e) {
            // TODO: handle exception



            return null;
        }

    }

    private String saveBitmapToFile(String folder, String fileName , Bitmap bitmap) {
        FileOutputStream out = null;
        File externalStorage = Environment.getExternalStorageDirectory();
        File file = new File(externalStorage.getAbsolutePath() + "/" + folder);

        try {
            if (!file.exists()) file.mkdirs();

            out = new FileOutputStream(file + "/" + fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file.getAbsolutePath() + "/" + fileName;
    }


}
