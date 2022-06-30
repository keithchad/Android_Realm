package com.chad.gads2022_java_kotlin.retrofit

import com.chad.gads2022_java_kotlin.models.Repository
import retrofit2.http.GET
import com.chad.gads2022_java_kotlin.models.SearchResponse
import retrofit2.Call
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface GithubAPIService {

    @GET("search/repositories")
    fun searchRepositories(@QueryMap options: MutableMap<String, String?>): Call<SearchResponse>

    @GET("/users/{username}/repos")
    fun searchRepositoriesByUser(@Path("username") githubUser: String): Call<List<Repository>>

}