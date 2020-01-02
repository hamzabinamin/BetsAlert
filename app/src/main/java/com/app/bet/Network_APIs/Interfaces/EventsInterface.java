package com.app.bet.Network_APIs.Interfaces;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EventsInterface {

    @GET("/v1/bet365/upcoming")
    Call<JsonObject> getUpcomingEvents(@Query("sport_id") String sport_id, @Query("token") String token);
}
