package com.bluelaned.submission2.retrofit

import com.bluelaned.submission2.response.DetailsResponse
import com.bluelaned.submission2.response.GithubResponse
import com.bluelaned.submission2.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getItemsItem(
        @Query("q") id: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailsResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}