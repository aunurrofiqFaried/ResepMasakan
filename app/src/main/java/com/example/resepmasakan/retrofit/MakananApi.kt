package com.example.resepmasakan.retrofit

import com.example.resepmasakan.pojo.CategoryList
import com.example.resepmasakan.pojo.MealByCategoryList
import com.example.resepmasakan.pojo.makananList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MakananApi {
    @GET("random.php")
    fun getRandomMeal():Call<makananList>
    @GET("lookup.php")
    fun getMealDetails(@Query("i") id:String):Call<makananList>
    @GET("filter.php")
    fun getPopularItems(@Query("c") categoryName:String):Call<MealByCategoryList>
    @GET("categories.php")
    fun getCategories() : Call<CategoryList>
    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String):Call<MealByCategoryList>
}