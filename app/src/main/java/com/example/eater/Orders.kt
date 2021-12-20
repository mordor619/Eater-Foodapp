package com.example.eater


data class Orders(
//    @SerializedName("id")
    var userId: Int = 0,
//    @SerializedName("name")
    var dishId: Int = 0,
//    @SerializedName("url")
    var orderId: Int = 0,
//    @SerializedName("type")
    var count: Int = 0,
//    @SerializedName("age")
    var dateTimestamp: Long = 0,
//    @SerializedName("vaccinated")

    )

data class OrderList(
    var dishOrders: MutableList<Orders>

)



data class Dish1(
    var id: Int = 0,
    var url:String = "",
    var name: String = "",
    var price: Int = 0,
    var contents:String="",
    var type :String="",
)

data class Dishes(
    var dishes: MutableList<Dish1>
)