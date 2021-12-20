package com.example.eater

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

data class Dish(
    //@JsonProperty("id")  //something new              //here is the error
    val id: Int = 0,
    val url: String = "",
    val name: String = "",
    val price: Int = 0,
    val contents: String = "",
    val type: String = ""
    )
