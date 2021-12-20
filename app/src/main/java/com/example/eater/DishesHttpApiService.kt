package com.example.eater

import retrofit2.http.GET

interface DishesHttpApiService {

    @GET("/eaterapp/dishes")
    suspend fun getAllDishes(): DishListResponse
}