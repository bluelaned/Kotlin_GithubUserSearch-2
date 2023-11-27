package com.bluelaned.submission2.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bluelaned.submission2.R
import com.bluelaned.submission2.adapter.SectionsPagerAdapter
import com.bluelaned.submission2.database.FavoriteUser
import com.bluelaned.submission2.databinding.ActivityDetailsBinding
import com.bluelaned.submission2.viewmodel.DetailViewModel
import com.bluelaned.submission2.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var detailViewModel: DetailViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("USERNAME")
        val avatar = intent.getStringExtra("AVATAR")

        detailViewModel = obtainDetailViewModel(this@DetailsActivity)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (username != null) {
            val sectionPagerAdapter = SectionsPagerAdapter(this, username)
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
        if (username != null) {
            detailViewModel.getDetailUser(username)
        }

        detailViewModel.userDetail.observe(this) {
            if (it != null) {
                Glide.with(this@DetailsActivity)
                    .load(it.avatarUrl)
                    .centerCrop()
                    .into(binding.imgUser)
                binding.tvName.text = it.name
                binding.tvUsername.text = it.username
                binding.tvTotalFollowers.text = "${it.followersCount} Follower"
                binding.tvTotalFollowing.text = "${it.followingCount} Following"
                binding.favoriteAdduser.contentDescription = it.isFavorite.toString()

                binding.apply {
                    if (!it.isFavorite) {
                        favoriteAdduser.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@DetailsActivity, R.drawable.baseline_favorite_border_24
                            )
                        )
                    } else {
                        favoriteAdduser.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@DetailsActivity, R.drawable.baseline_favorite_24
                            )
                        )
                    }
                }
            }
        }

        detailViewModel.loading.observe(this) {
            showLoading(it)
        }

        binding.apply {
            favoriteAdduser.setOnClickListener {
                val userFavorite = FavoriteUser(
                    name = tvName.text.toString(),
                    username = tvUsername.text.toString(),
                    avatarUrl = avatar.toString(),
                    isFavorite = true,
                    followersCount = tvTotalFollowers.text.toString(),
                    followingCount = tvTotalFollowing.text.toString()
                )

                val currentIcon = favoriteAdduser.contentDescription
                if (currentIcon.equals("true")) {
                    favoriteAdduser.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@DetailsActivity, R.drawable.baseline_favorite_border_24
                        )
                    )
                    detailViewModel.deleteUserFavorite(userFavorite)
                    favoriteAdduser.contentDescription = "false"
                } else {
                    favoriteAdduser.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@DetailsActivity, R.drawable.baseline_favorite_24
                        )
                    )
                    detailViewModel.insertUserFavorite(userFavorite)
                    favoriteAdduser.contentDescription = "true"
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun obtainDetailViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(this@DetailsActivity, factory)[DetailViewModel::class.java]
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower_fragment,
            R.string.following_fragment
        )
    }
}
