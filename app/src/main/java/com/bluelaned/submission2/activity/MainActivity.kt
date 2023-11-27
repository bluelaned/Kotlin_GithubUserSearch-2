package com.bluelaned.submission2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluelaned.submission2.R
import com.bluelaned.submission2.adapter.UserAdapter
import com.bluelaned.submission2.databinding.ActivityMainBinding
import com.bluelaned.submission2.response.ItemsItem
import com.bluelaned.submission2.settings.SettingPreferences
import com.bluelaned.submission2.settings.SettingViewModel
import com.bluelaned.submission2.settings.SettingViewModelFactory
import com.bluelaned.submission2.settings.SettingsActivity
import com.bluelaned.submission2.settings.dataStore
import com.bluelaned.submission2.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        binding.searchBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu1 -> {
                    val intent = Intent(this, FavoriteUserActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu2 -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    mainViewModel.getListSearch(searchView.text.toString())
                    mainViewModel.listSearch.observe(this@MainActivity) { items ->
                        if (items.isNullOrEmpty()) {
                            showNotFound(true)
                        } else {
                            showNotFound(false)
                        }
                    }
                    false
                }
        }


        mainViewModel.loading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.listSearch.layoutManager = layoutManager


        mainViewModel.listSearch.observe(this) { items ->
            if (items != null) {
                setUserData(items)
            }
        }
    }

    private fun setUserData(datauser: List<ItemsItem>) {
        val adapter = UserAdapter(datauser)
        adapter.submitList(datauser)
        binding.listSearch.adapter = adapter
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

    private fun showNotFound(isDataNotFound: Boolean) {
        binding.apply {
            if (isDataNotFound) {
                listSearch.visibility = View.GONE
            } else {
                listSearch.visibility = View.VISIBLE
            }
        }
    }
}