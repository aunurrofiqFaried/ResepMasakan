package com.example.resepmasakan.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resepmasakan.R
import com.example.resepmasakan.activities.CategoryMakananActivity
import com.example.resepmasakan.activities.MainActivity
import com.example.resepmasakan.adapters.CategoriesAdapter
import com.example.resepmasakan.databinding.FragmentCategoriesBinding
import com.example.resepmasakan.videoModel.HomeViewModel

class CategoriesFragment : Fragment() {
    private lateinit var binding:FragmentCategoriesBinding
    private lateinit var categoriesAdapter:CategoriesAdapter
    private lateinit var viewModel:HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecycleView()
        observeCategories()

        prepareCategoriesRecycleView()
        viewModel.getCategories()
        observeCategoriesLiveData()

        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMakananActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecycleView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recycleViewCategory.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer{ categories->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun observeCategories() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun prepareRecycleView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recycleViewCategory.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }
}