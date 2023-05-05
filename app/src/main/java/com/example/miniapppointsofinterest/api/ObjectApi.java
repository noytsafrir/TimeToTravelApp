package com.example.miniapppointsofinterest.api;

import com.example.miniapppointsofinterest.model.object.ObjectBoundary;
import com.example.miniapppointsofinterest.model.user.NewUserBoundary;
import com.example.miniapppointsofinterest.model.user.UserBoundary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ObjectApi {

    @POST("superapp/objects")
    Call<ObjectBoundary> createObject(@Body ObjectBoundary objectBoundary);

    @GET("superapp/objects/{superapp}/{InternalObjectId}")
    Call<ObjectBoundary> getObject(@Path("superapp") String superapp, @Path("InternalObjectId") String id);

    @GET("superapp/objects")
    Call<List<ObjectBoundary>> getAllObjects();

    @PUT("superapp/objects/{superapp}/{InternalObjectId}")
    void updateObject(@Path("superapp") String superapp, @Path("InternalObjectId") String id, @Body ObjectBoundary objectBoundary);
}