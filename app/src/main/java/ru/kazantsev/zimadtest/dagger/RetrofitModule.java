package ru.kazantsev.zimadtest.dagger;

import android.content.Context;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import ru.kazantsev.zimadtest.service.RestService;

/**
 * Date: 8/26/2016
 * Time: 12:28
 *
 * @author Artur Artikov
 */
@Module
public class RetrofitModule {

	@Provides
	@Singleton
	public Retrofit provideRetrofit(Retrofit.Builder builder) {
		return builder.baseUrl(RestService.BASE_URL).build();
	}

	@Provides
	@Singleton
	public OkHttpClient.Builder provideOkHttpBuilder(Context context) {
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		return new OkHttpClient.Builder()
				.connectTimeout(3, TimeUnit.SECONDS)
				.readTimeout(20, TimeUnit.SECONDS)
				.addNetworkInterceptor(interceptor)
				.addInterceptor(chain -> {
					Request original = chain.request();
					Request request = original.newBuilder()
							.header("X-Requested-With", "XMLHttpRequest")
							.header("Content-Type", "application/json; charset=utf-8")
							.header("Accept", "application/json")
							.method(original.method(), original.body())
							.build();
					return chain.proceed(request);
				});
	}

	@Provides
	@Singleton
	public Retrofit.Builder provideRetrofitBuilder(Converter.Factory converterFactory, OkHttpClient.Builder builder) {
		return new Retrofit.Builder()
				.client(builder.build())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(converterFactory);
	}

	@Provides
	@Singleton
	public Converter.Factory provideConverterFactory(Gson gson) {
		return GsonConverterFactory.create(gson);
	}

	@Provides
	@Singleton
	Gson provideGson() {
		return new GsonBuilder()
				.setPrettyPrinting()
				.serializeNulls()
				.create();
	}

}
