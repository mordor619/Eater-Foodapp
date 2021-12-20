package com.example.eater

import retrofit2.Call
import retrofit2.Response
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface Api {
//    @FormUrlEncoded
    @POST("/eaterapp/login")
    suspend fun userLogin(@Body requestBody: RequestBody) : Response<ResponseBody>

    @POST("/eaterapp/register")
    suspend fun userRegister(@Body requestBody: RequestBody) : Response<ResponseBody>



    @DELETE("users/me")
    suspend fun userDelete() : Response<ResponseBody>



    @POST("users/me/email")
    suspend fun changeEmail(@Body requestBody: RequestBody) : Response<ResponseBody>

}