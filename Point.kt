package com.example.test2

import android.util.Log

class Point(var x: Int, var y: Int) {

    fun print() {
        Log.v("Elad",x.toString() + " " + y.toString());
    }
}