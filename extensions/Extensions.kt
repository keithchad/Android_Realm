package com.chad.gads2022_java_kotlin.extensions

import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.chad.gads2022_java_kotlin.models.ErrorResponse
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import java.io.IOException


//Showing Error Message in Response
fun Context.showErrorMessage(errorBody: ResponseBody,  duration: Int = Toast.LENGTH_SHORT) {
    val gson = GsonBuilder().create()
    try {
        val errorResponse: ErrorResponse = gson.fromJson(errorBody.string(), ErrorResponse::class.java)
        errorResponse.message?.let { toast(it, duration) }
    } catch (e: IOException) {
        Log.i("Exception ", e.toString())
    }
}

//Showing Toast
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

//Edittext Validation
fun EditText.isNotEmpty(textInputLayout: TextInputLayout) : Boolean {
    return if(text.toString().isEmpty()) {
        textInputLayout.error = "Blank"
        false
    } else {
        textInputLayout.isErrorEnabled = false
        true
    }
}