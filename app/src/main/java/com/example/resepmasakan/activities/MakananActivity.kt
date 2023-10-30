package com.example.resepmasakan.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.resepmasakan.R
import com.example.resepmasakan.databinding.ActivityMakananBinding
import com.example.resepmasakan.db.MealDatabase
import com.example.resepmasakan.fragments.HomeFragment
import com.example.resepmasakan.pojo.Meal
import com.example.resepmasakan.videoModel.MakananViewModel
import com.example.resepmasakan.videoModel.MakananViewModelFactory

class MakananActivity : AppCompatActivity() {
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var binding:ActivityMakananBinding
    private lateinit var youtubeLink:String
    private lateinit var mealMvvm:MakananViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MakananViewModelFactory(mealDatabase)

        mealMvvm = ViewModelProvider(this,viewModelFactory)[MakananViewModel::class.java]

        getMakananInfoFromIntent()
        setInfoInViews()

        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observerMakananDetailsLiveData()
        onYoutubeClick()

        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btnTambahFavorit.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this,"Makan Telah Disimpan ke Favorit", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onYoutubeClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave:Meal?=null
    private fun observerMakananDetailsLiveData() {
        mealMvvm.observerMakananDetailsLiveData().observe(this, object : Observer<Meal>{
            override fun onChanged(t: Meal?) {
                onResponsecase()
                val meal = t
                mealToSave = meal

                binding.tvKategori.text = "Kategori : ${meal!!.strCategory}"
                binding.tvKhas.text = "Khas : ${meal.strArea}"
                binding.tvTataCara.text = meal.strInstructions

                youtubeLink = meal.strYoutube
            }
        })
    }

    private fun setInfoInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMakananDetail)

        binding.collapsingToolBar.title = mealName
        binding.collapsingToolBar.setExpandedTitleColor(resources.getColor(R.color.white))
        binding.collapsingToolBar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
    }

    private fun getMakananInfoFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase(){
        binding.progresBar.visibility = View.VISIBLE
        binding.btnTambahFavorit.visibility = View.INVISIBLE
        binding.tvTataCara.visibility = View.INVISIBLE
        binding.tvKategori.visibility = View.INVISIBLE
        binding.tvKhas.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }
    private fun onResponsecase(){
        binding.progresBar.visibility = View.INVISIBLE
        binding.btnTambahFavorit.visibility = View.VISIBLE
        binding.tvTataCara.visibility = View.VISIBLE
        binding.tvKategori.visibility = View.VISIBLE
        binding.tvKhas.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}