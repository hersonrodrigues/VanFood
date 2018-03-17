package com.example.stefanini.challengevanhack.api;

import com.example.stefanini.challengevanhack.model.Customer;
import com.example.stefanini.challengevanhack.model.Order;
import com.example.stefanini.challengevanhack.model.Product;
import com.example.stefanini.challengevanhack.model.Store;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Herson Rodrigues <hersonrodrigues@gmail.com> on 17/03/2018.
 */

public interface IRestApi {

    @POST("/api/v1/Customer")
    Call<JsonObject> customerForm(
            @Body Customer customer
    );

    @POST("/api/v1/Customer/auth")
    Call<Object> loginForm(
            @Query("email") String email,
            @Query("password") String password
    );

    @GET("/api/v1/Store")
    Call<List<Store>> listStories();

    @GET("/api/v1/Product")
    Call<List<Product>> listProducts();

    @POST("/api/v1/Order")
    Call<JsonObject> submitStore(
            @Header("Authorization") String authHeader,
            @Body Order order
    );
}
