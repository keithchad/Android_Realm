package com.chad.gads2022_java_kotlin.app

import android.content.Context
import android.util.Log
import okhttp3.ResponseBody
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.chad.gads2022_java_kotlin.models.ErrorResponse
import android.widget.Toast
import java.io.IOException

object Util {
    fun showErrorMessage(context: Context?, errorBody: ResponseBody) {
        val gson = GsonBuilder().create()
        val errorResponse: ErrorResponse
        try {
            errorResponse = gson.fromJson(errorBody.string(), ErrorResponse::class.java)
            showMessage(context, errorResponse.message)
        } catch (e: IOException) {
            Log.i("Exception ", e.toString())
        }
    }

    @JvmStatic
	fun showMessage(context: Context?, msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}