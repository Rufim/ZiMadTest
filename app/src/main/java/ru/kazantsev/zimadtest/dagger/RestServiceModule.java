package ru.kazantsev.zimadtest.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import ru.kazantsev.zimadtest.net.RestApi;
import ru.kazantsev.zimadtest.service.RestService;

@Module(includes = {RestApiModule.class})
public class RestServiceModule {

    @Provides
    @Singleton
    public RestService provideRestService(RestApi authApi) {
        return new RestService(authApi);
    }

}
