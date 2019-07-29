package ru.kazantsev.zimadtest.dagger;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import ru.kazantsev.zimadtest.net.RestApi;

/**
 * Date: 9/2/2016
 * Time: 18:54
 *
 * @author Artur Artikov
 */
@Module(includes = {RetrofitModule.class})
public class RestApiModule {
	@Provides
	@Singleton
	public RestApi provideAuthApi(Retrofit retrofit) {
		return retrofit.create(RestApi.class);
	}
}
