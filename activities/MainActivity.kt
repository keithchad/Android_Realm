package com.chad.gads2022_java_kotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import android.os.Bundle
import com.chad.gads2022_java_kotlin.R
import io.realm.RealmConfiguration
import android.content.SharedPreferences
import android.content.Intent
import android.view.View
import com.chad.gads2022_java_kotlin.activities.DisplayActivity
import com.chad.gads2022_java_kotlin.app.Constants
import io.realm.Realm

class MainActivity : AppCompatActivity() {
    private var etName: EditText? = null
    private var etGithubRepoName: EditText? = null
    private var etLanguage: EditText? = null
    private var etGithubUser: EditText? = null
    private var inputLayoutName: TextInputLayout? = null
    private var inputLayoutRepoName: TextInputLayout? = null
    private var inputLayoutGithubUser: TextInputLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeRealm()
        initialize()
    }

    //Initialize Views
    private fun initialize() {
        etName = findViewById(R.id.etName)
        etGithubRepoName = findViewById(R.id.etRepoName)
        etLanguage = findViewById(R.id.etLanguage)
        etGithubUser = findViewById(R.id.etGithubUser)
        inputLayoutName = findViewById(R.id.inputLayoutName)
        inputLayoutRepoName = findViewById(R.id.inputLayoutRepoName)
        inputLayoutGithubUser = findViewById(R.id.inputLayoutGithubUser)
    }

    //Initialize Realm
    private fun initializeRealm() {
        Realm.init(this) // should only be done once when app starts
        val config = RealmConfiguration.Builder()
            .name("myrealm.realm")
            .allowWritesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)
    }

    // Save app username in SharedPreferences
    fun saveName(view: View?) {
        if (isNotEmpty(etName, inputLayoutName)) {
            val personName = etName!!.text.toString()
            val sp = getSharedPreferences(Constants.APP_SHARED_PREFERENCES, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString(Constants.KEY_PERSON_NAME, personName)
            editor.apply()
        }
    }

    // Search repositories on github
    fun listRepositories(view: View?) {
        if (isNotEmpty(etGithubRepoName, inputLayoutRepoName)) {
            val queryRepo = etGithubRepoName!!.text.toString()
            val repoLanguage = etLanguage!!.text.toString()
            val intent = Intent(this@MainActivity, DisplayActivity::class.java)
            intent.putExtra(Constants.KEY_QUERY_TYPE, Constants.SEARCH_BY_REPO)
            intent.putExtra(Constants.KEY_REPO_SEARCH, queryRepo)
            intent.putExtra(Constants.KEY_LANGUAGE, repoLanguage)
            startActivity(intent)
        }
    }

    //Search repositories of a particular github user
    fun listUserRepositories(view: View?) {
        if (isNotEmpty(etGithubUser, inputLayoutGithubUser)) {
            val githubUser = etGithubUser!!.text.toString()
            val intent = Intent(this@MainActivity, DisplayActivity::class.java)
            intent.putExtra(Constants.KEY_QUERY_TYPE, Constants.SEARCH_BY_USER)
            intent.putExtra(Constants.KEY_GITHUB_USER, githubUser)
            startActivity(intent)
        }
    }

    //Validation
    private fun isNotEmpty(editText: EditText?, textInputLayout: TextInputLayout?): Boolean {
        return if (editText!!.text.toString().isEmpty()) {
            textInputLayout!!.error = "Cannot be blank !"
            false
        } else {
            textInputLayout!!.isErrorEnabled = false
            true
        }
    }
}