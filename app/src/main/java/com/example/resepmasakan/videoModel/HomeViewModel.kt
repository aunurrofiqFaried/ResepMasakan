package com.example.resepmasakan.videoModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resepmasakan.db.MealDatabase
import com.example.resepmasakan.pojo.*
import com.example.resepmasakan.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
):ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()

    private var saveStateRandomMakanan : Meal ?= null
    fun getRandomMakanan(){
        saveStateRandomMakanan?.let { randomMeal ->
            randomMealLiveData.postValue(randomMeal)
            return
        }
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<makananList> {
            override fun onResponse(call: Call<makananList>, response: Response<makananList>) {
                if (response.body() !=null){
                    val randomMakanan: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMakanan
                    saveStateRandomMakanan = randomMakanan
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<makananList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }
        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Beef").enqueue(object : Callback<MealByCategoryList> {
            override fun onResponse(call: Call<MealByCategoryList>, response: Response<MealByCategoryList>) {
                if (response.body() != null){
                    popularItemsLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }

        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeViewModel",t.message.toString())
            }

        })
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun observeRandomMakananLiveData():LiveData<Meal>{
        return randomMealLiveData
    }

    fun observerPopularItemsLiveData():LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }

    fun observeCategoriesLiveData():LiveData<List<Category>>{
        return categoriesLiveData
    }

    fun observeFavotitesMealsLiveData():LiveData<List<Meal>>{
        return favoritesMealsLiveData
    }
}