package com.example.miniapppointsofinterest.api;

import com.example.miniapppointsofinterest.model.miniappCommand.SuperAppObjectIdBoundary;
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
import retrofit2.http.Query;

public interface ObjectApi {

    @POST("superapp/objects")
    Call<ObjectBoundary> createObject(@Body ObjectBoundary objectBoundary);

    @GET("/superapp/objects")
    Call<List<ObjectBoundary>> getAllObjectsUsingPagination(@Query("userSuperapp") String userSuperapp,
                                                            @Query("userEmail") String email,
                                                            @Query("size") int size,
                                                            @Query("page") int page);

    @GET("/superapp/objects/search/byType/{type}")
    Call<List<ObjectBoundary>> getObjectByType(@Path("type") String type,
                                               @Query("userSuperapp") String userSuperapp,
                                               @Query("userEmail") String email,
                                               @Query("size") int size,
                                               @Query("page") int page);
    @PUT("/superapp/objects/{superapp}/{internalObjectId}?userSuperapp={userSuperapp}&userEmail={email}")
    void updateObject(@Path("superapp") String superapp,
                      @Path("internalObjectId") String id,
                      @Path("userSuperapp") String userSuperapp,
                      @Path("email") String email,
                      @Body ObjectBoundary objectBoundary);

    @GET("/superapp/objects/{superapp}/{internalObjectId}?userSuperapp={userSuperapp}&userEmail={email}")
    Call<ObjectBoundary> getSpecificObject(@Path("superapp") String superapp,
                                           @Path("internalObjectId") String id,
                                           @Path("userSuperapp") String userSuperapp,
                                           @Path("email") String email);




}