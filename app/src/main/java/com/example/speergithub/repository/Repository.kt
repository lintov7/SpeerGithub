package com.example.speergithub.repository

import com.example.speergithub.repository.services.GithubServices
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/*
* Repository for connecting to the network or local database
* */
class Repository @Inject constructor(private val githubServices: GithubServices) {
    suspend fun findUser(username:String) = githubServices.findUser(username)

    suspend fun getFollowers(username:String, page: Int, perPage: Int) = githubServices.getFollowers(username, page, perPage)

    suspend fun getFollowing(username:String, page: Int, perPage: Int) = githubServices.getFollowing(username, page, perPage)
}