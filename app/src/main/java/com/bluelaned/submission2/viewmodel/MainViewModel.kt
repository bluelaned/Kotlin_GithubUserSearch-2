package com.bluelaned.submission2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluelaned.submission2.response.GithubResponse
import com.bluelaned.submission2.response.ItemsItem
import com.bluelaned.submission2.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listSearch: LiveData<List<ItemsItem>> = _listUser

    private val _loadingScreen = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loadingScreen

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        getListSearch("A")
    }

    fun getListSearch(query: String) {
        _loadingScreen.value = true
        val client = ApiConfig.getApiService().getItemsItem(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _loadingScreen.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _loadingScreen.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}