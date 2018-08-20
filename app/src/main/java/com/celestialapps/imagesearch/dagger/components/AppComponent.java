package com.celestialapps.imagesearch.dagger.components;

import com.celestialapps.imagesearch.dagger.modules.ApiModule;
import com.celestialapps.imagesearch.dagger.modules.ContextModule;
import com.celestialapps.imagesearch.dagger.modules.RealmModule;
import com.celestialapps.imagesearch.dagger.modules.RetrofitModule;
import com.celestialapps.imagesearch.presentation.presenter.GlidePresenter;
import com.celestialapps.imagesearch.presentation.presenter.ImageSearchPresenter;

import javax.inject.Singleton;

import dagger.Component;


@Component(modules = {ContextModule.class, ApiModule.class, RetrofitModule.class, RealmModule.class})
@Singleton
public interface AppComponent {


    void inject(ImageSearchPresenter imageSearchPresenter);

    void inject(GlidePresenter glidePresenter);
}
