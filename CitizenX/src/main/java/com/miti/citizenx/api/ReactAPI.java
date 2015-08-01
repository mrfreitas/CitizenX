package com.miti.citizenx.api;

import com.miti.citizenx.model.UserEntity;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * mrfreitas
 * Date: 27/07/2015
 * Time: 00:34
 */
public interface ReactAPI
{
    @GET("/reacts")
    void getReacts(Callback<UserEntity> response);

    @GET("/reacts")
    void getReact(@Query("reactId") String reactId, Callback<UserEntity> response);

    @POST("/reacts")
    void setReacts(@Body UserAPI userAPI, Callback<UserAPI> cb);
}
