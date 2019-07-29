package ru.kazantsev.zimadtest.net;


import io.reactivex.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.kazantsev.zimadtest.model.Message;


/**
 * Date: 18.01.2016
 * Time: 12:07
 *
 * @author Yuri Shmakov
 */
public interface RestApi {

    enum PetQuery {
        cat, dog;
    }

    @GET("/xim/api.php")
    Observable<Message> getPets(@Query("query") PetQuery query);

}
