package com.example.resepmasakan.videoModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resepmasakan.activities.MakananActivity
import com.example.resepmasakan.db.MealDatabase
import com.example.resepmasakan.pojo.Meal
import com.example.resepmasakan.pojo.makananList
import com.example.resepmasakan.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakananViewModel(
    val mealDatabase: MealDatabase
):ViewModel() {
    private var mealDetailsLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<makananList>{
            override fun onResponse(call: Call<makananList>, response: Response<makananList>) {
                if (response.body()!=null){
                    mealDetailsLiveData.value = response.body()!!.meals[0]
                }else {
                    return
                }
            }

            override fun onFailure(call: Call<makananList>, t: Throwable) {
                Log.d("MakananActivity",t.message.toString())
            }
        })
    }

    fun observerMakananDetailsLiveData():LiveData<Meal>{
        return mealDetailsLiveData
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }
}