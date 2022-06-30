package com.chad.gads2022_java_kotlin.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.chad.gads2022_java_kotlin.R
import com.chad.gads2022_java_kotlin.app.Constants
import com.google.android.material.textfield.TextInputLayout
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialize Realm
        initializeRealm()

        //Views
        initialize()

    }

    //Initialize Views
    private fun initialize() {
        setSupportActionBar(toolbar_main)

        //Save On Click Listener
        saveNameButton.setOnClickListener {

            if(isNotEmpty(etName, inputLayoutName)) {

                val name = etName.text.toString()
                val sharedPreferences = getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString(Constants.KEY_PERSON_NAME, name)
                editor.apply()

            }


        }

        //Repositories On Click Listener
        listRepositoriesButton.setOnClickListener {
            if(isNotEmpty(etRepoName, inputLayoutRepoName)) {

                val repoName = etRepoName.text.toString()
                val language = etLanguage.text.toString()

                val intent = Intent(this@MainActivity, DisplayActivity::class.java)
                intent.putExtra(Constants.KEY_QUERY_TYPE, Constants.SEARCH_BY_REPO)
                intent.putExtra(Constants.KEY_REPO_SEARCH, repoName)
                intent.putExtra(Constants.KEY_LANGUAGE, language)
                startActivity(intent)

            }

        }

        //User Repositories On Click Listener
        listUserRepositoriesButton.setOnClickListener {

            if(isNotEmpty(etGithubUser, inputLayoutGithubUser)) {
                val githubUser = etGithubUser.text.toString()

                val intent = Intent(this@MainActivity, DisplayActivity::class.java)
                intent.putExtra(Constants.KEY_QUERY_TYPE, Constants.SEARCH_BY_USER)
                intent.putExtra(Constants.KEY_GITHUB_USER, githubUser)
                startActivity(intent)
            }

        }

    }

    //Validation
    private fun isNotEmpty(edittext: EditText, textInputLayout: TextInputLayout) : Boolean {
        if(edittext.text.toString().isEmpty()) {
            textInputLayout.error = "Blank"
            return false
        } else {
            textInputLayout.isErrorEnabled = false
            return true
        }
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


}