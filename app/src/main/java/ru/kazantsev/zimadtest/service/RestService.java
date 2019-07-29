package ru.kazantsev.zimadtest.service;


import io.reactivex.Observable;

import ru.kazantsev.zimadtest.model.Message;
import ru.kazantsev.zimadtest.model.Pet;
import ru.kazantsev.zimadtest.net.RestApi;


public class RestService {

    public static String BASE_URL = "http://kot3.com";

    private RestApi restApi;

    public RestService(RestApi restApi) {
        this.restApi = restApi;
    }


    public Observable<Message> getPets(RestApi.PetQuery petQuery) {
        return restApi.getPets(petQuery);
    }

}
