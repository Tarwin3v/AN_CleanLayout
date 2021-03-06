package com.example.cleanlayout.util

import android.util.Log
import com.example.cleanlayout.util.Constants.DEBUG
import com.example.cleanlayout.util.Constants.TAG

var isUnitTest = false

fun printLogD(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}
