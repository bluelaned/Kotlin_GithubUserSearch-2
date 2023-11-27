package com.bluelaned.submission2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluelaned.submission2.database.FavoriteUser
import com.bluelaned.submission2.database.UserRepository
import com.bluelaned.submission2.response.DetailsResponse
import com.bluelaned.submission2.response.ItemsItem
import com.bluelaned.submission2.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val userRepository: UserRepository = UserRepository(application)

    private val _userDetail = MutableLiveData<FavoriteUser>()
    val userDetail: LiveData<FavoriteUser> = _userDetail

    private val _loadingScreen = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loadingScreen

    private val _userFollower = MutableLiveData<List<ItemsItem>>()
    val userFollower: LiveData<List<ItemsItem>> = _userFollower

    private val _userFollowing = MutableLiveData<List<ItemsItem>>()
    val userFollowing: LiveData<List<ItemsItem>> = _userFollowing

    private var isloaded = false
    private var isfollowerloaded = false
    private var isfollowingloaded = false

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun insertUserFavorite(favoriteUser: FavoriteUser) {
        userRepository.insert(favoriteUser)
    }

    fun deleteUserFavorite(favoriteUser: FavoriteUser) {
        userRepository.delete(favoriteUser)
    }

    fun getDetailUser(username: String) {
        if (!isloaded) {
            _loadingScreen.value = true
            val client = ApiConfig.getApiService().getDetailUser(username)
            client.enqueue(object : Callback<DetailsResponse> {
                override fun onResponse(
                    call: Call<DetailsResponse>,
                    response: Response<DetailsResponse>
                ) {
                    _loadingScreen.value = false
                    if (response.isSuccessful) {
                        val resBody = response.body()
                        if (resBody != null) {
                            viewModelScope.launch {
                                val isFavoriteUser =
                                    userRepository.isFavorite(resBody.login)
                                val currentUser = FavoriteUser(
                                    username = resBody.login,
                                    name = resBody.name,
                                    avatarUrl = resBody.avatarUrl,
                                    followersCount = resBody.followers.toString(),
                                    followingCount = resBody.following.toString(),
                                    isFavorite = isFavoriteUser
                                )
                                _userDetail.postValue(currentUser)
                            }
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DetailsResponse>, t: Throwable) {
                    _loadingScreen.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
            isloaded = true
        }
    }

    fun getFollower(username: String) {
        if (!isfollowerloaded) {
            _loadingScreen.value = true
            val client = ApiConfig.getApiService().getFollowers(username)
            client.enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _loadingScreen.value = false
                    if (response.isSuccessful) {
                        _userFollower.postValue(response.body())

                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _loadingScreen.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
            isfollowerloaded = true
        }
    }

    fun getFollowing(username: String) {
        if (!isfollowingloaded) {
            _loadingScreen.value = true
            val client = ApiConfig.getApiService().getFollowing(username)
            client.enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _loadingScreen.value = false
                    if (response.isSuccessful) {
                        _userFollowing.postValue(response.body())
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _loadingScreen.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
            isfollowingloaded = true
        }
    }
}