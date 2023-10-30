package com.example.resepmasakan.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.resepmasakan.R
import com.example.resepmasakan.activities.MainActivity
import com.example.resepmasakan.adapters.FavoritesMealsAdapter
import com.example.resepmasakan.databinding.FragmentFavoritesBinding
import com.example.resepmasakan.videoModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {
    private lateinit var binding:FragmentFavoritesBinding
    private lateinit var viewModel:HomeViewModel
    private lateinit var favoritesAdapter:FavoritesMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        prepareRecycleView()
        observeFavorites()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteMeal(favoritesAdapter.differ.currentList[position])

                Snackbar.make(requireView(),"Makanan Favorit Dihapus",Snackbar.LENGTH_LONG).show()
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recycleViewFavorites)
    }

    private fun prepareRecycleView() {
        favoritesAdapter = FavoritesMealsAdapter()
        binding.recycleViewFavorites.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = favoritesAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.observeFavotitesMealsLiveData().observe(requireActivity(), Observer { meals ->
            favoritesAdapter.differ.submitList(meals)
        })
    }
}