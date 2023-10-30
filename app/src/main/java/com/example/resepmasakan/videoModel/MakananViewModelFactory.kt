package com.example.resepmasakan.videoModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.resepmasakan.db.MealDatabase

class MakananViewModelFactory(
    private val mealDatabase: MealDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MakananViewModel(mealDatabase) as T
    }
}