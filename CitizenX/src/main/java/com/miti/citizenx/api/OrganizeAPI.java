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
 * Time: 01:39
 */
public interface OrganizeAPI
{
    @GET("/organize")
    void getOrganizes(Callback<UserEntity> response);

    @GET("/organize")
    void getOrganize(@Query("organizeId") String reactId, Callback<UserEntity> response);

    @POST("/organize")
    void setOrganize(@Body UserAPI userAPI, Callback<UserAPI> cb);
}
