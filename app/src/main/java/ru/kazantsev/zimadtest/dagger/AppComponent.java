package ru.kazantsev.zimadtest.dagger;


import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import javax.inject.Singleton;

import dagger.Component;
import ru.kazantsev.zimadtest.activity.MainActivity;
import ru.kazantsev.zimadtest.fragment.PetTabFragment;
import ru.kazantsev.zimadtest.service.RestService;

/**
 * Created by Dmitry on 28.06.2016.
 */

@Singleton
@Component(modules = {ContextModule.class, RestServiceModule.class})
public interface AppComponent {
    Context getContext();
    RestService getRestService();

    void inject(PetTabFragment petTabFragment);

}
