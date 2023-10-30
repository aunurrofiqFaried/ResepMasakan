package com.example.resepmasakan.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.resepmasakan.databinding.FragmentHomeBinding
import com.example.resepmasakan.pojo.Meal
import com.example.resepmasakan.videoModel.HomeViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.resepmasakan.activities.CategoryMakananActivity
import com.example.resepmasakan.activities.MainActivity
import com.example.resepmasakan.activities.MakananActivity
import com.example.resepmasakan.adapters.CategoriesAdapter
import com.example.resepmasakan.adapters.MostPopularAdapter
import com.example.resepmasakan.pojo.MealsByCategory

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var viewModel:HomeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemsAdapter:MostPopularAdapter
    private lateinit var categoriesAdapter:CategoriesAdapter

    companion object{
        const val MEAL_ID = "com.example.resepmasakan.fragments.idMeal"
        const val MEAL_NAME = "com.example.resepmasakan.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.resepmasakan.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.resepmasakan.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRandomMakanan()
        observerRandomMakanan()
        onRandomMakananKlik()

        viewModel.getPopularItems()
        preparePopularItemsRecycleView()
        observePopularItemsLiveData()
        onPopularItemClick()

        prepareCategoriesRecycleView()
        viewModel.getCategories()
        observeCategoriesLiveData()

        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity,CategoryMakananActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecycleView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recycleViewtv3.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer{ categories->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal->
            val intent = Intent(activity,MakananActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecycleView() {
        binding.recycleViewtv2.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observerPopularItemsLiveData().observe(viewLifecycleOwner,
            { mealList->
                popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)
            })
    }

    private fun onRandomMakananKlik() {
        binding.imgRandom.setOnClickListener {
            val intent = Intent(activity,MakananActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMakanan() {
        viewModel.observeRandomMakananLiveData().observe(viewLifecycleOwner,
            { meal -> Glide.with(this@HomeFragment) .load(meal!!.strMealThumb).into(binding.imgRandom)
                this.randomMeal = meal
            })
    }
}