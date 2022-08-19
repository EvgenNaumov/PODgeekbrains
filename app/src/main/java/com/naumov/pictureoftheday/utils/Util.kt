package com.naumov.pictureoftheday.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.naumov.pictureoftheday.BuildConfig
const val TODAY = 1
const val YESTERDAY = 2
const val DBY = 3

val DEBUG:Boolean = BuildConfig.DEBUG && true
const val baseUrl = "https://api.nasa.gov/"
const val TAG = "@@@"
fun View.toast(string: String?, context:Context) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.BOTTOM, 0, 250)
        show()
    }
}
