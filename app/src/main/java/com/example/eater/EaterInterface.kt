package com.example.eater

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface EaterInterface {

//amit
    @GET("users/me/orders")
//    suspend fun getPets(): Response<PetList>
    suspend fun getMyorders(): OrderList
//    suspend fun getPets(): List<Pets>
//    suspend fun getPets(token: String): List<Pets>


    @GET("dishes")
    suspend fun getdishes(): Dishes


    @DELETE("users/me/orders/{id}")
    suspend fun deleteOrder(@Path("id") id: Int): Response<ResponseBody>


    //achal

    @GET("users")
//    suspend fun getPets(): Response<PetList>
    suspend fun getUsers(): Users


    @GET("users/me/loginHistory")
    suspend fun getHistory(): History


    @GET("dishes")
    suspend fun getDishes(): com.example.eater.Dishes


    @POST("users/me/orders")
    suspend fun createOrder(@Body requestBody: RequestBody) : Response<ResponseBody>


}

