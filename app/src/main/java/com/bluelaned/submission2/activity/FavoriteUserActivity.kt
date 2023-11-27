package com.bluelaned.submission2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluelaned.submission2.adapter.FavoriteUserAdapter
import com.bluelaned.submission2.databinding.ActivityFavoriteUserBinding
import com.bluelaned.submission2.viewmodel.FavoriteUserViewModel
import com.bluelaned.submission2.viewmodel.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var favoriteUserViewModel: FavoriteUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Favorite"

        val layoutManager = LinearLayoutManager(this)
        binding.rvUserFavorite.layoutManager = layoutManager

        favoriteUserViewModel = obtainFavoriteUserViewModel(this@FavoriteUserActivity)
        favoriteUserViewModel.getAllFavoriteUsers().observe(this) {
            val adapter = FavoriteUserAdapter()
            adapter.submitList(it)
            binding.rvUserFavorite.adapter = adapter
        }
    }

    private fun obtainFavoriteUserViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(
            this@FavoriteUserActivity,
            factory
        )[FavoriteUserViewModel::class.java]
    }
}