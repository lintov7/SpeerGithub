package com.example.speergithub.repository.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
/*
* User Model serializable with GSON
* */
data class User(@SerializedName("login")val username:String, @SerializedName("avatar_url")val avatar: String?, @SerializedName("name") val name: String?, @SerializedName("bio") val description:String?, @SerializedName("followers") val followers: Int, @SerializedName("following") val following:Int): Serializable