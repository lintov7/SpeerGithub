package com.example.speergithub.repository.services

import com.example.speergithub.repository.models.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/*
* Api Interface
* */
interface GithubServices {
    @GET("/users/{username}")
    suspend fun findUser(@Path("username") username:String): Response<User?>

    @GET("/users/{username}/followers")
    suspend fun getFollowers(@Path("username") username:String, @Query("page") page: Int, @Query("per_page") perPage: Int): Response<List<User>>

    @GET("/users/{username}/following")
    suspend fun getFollowing(@Path("username") username:String, @Query("page") page: Int, @Query("per_page") perPage: Int): Response<List<User>>
}