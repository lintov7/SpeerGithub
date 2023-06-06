package com.example.speergithub.util

import android.content.Context
import android.widget.Toast

/*
* Extension function to show toast message
* */
fun Context.showToast(message:String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}