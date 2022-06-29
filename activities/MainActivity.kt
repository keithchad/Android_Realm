package com.chad.gads2022_java_kotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chad.gads2022_java_kotlin.R
import io.realm.RealmConfiguration
import android.content.Intent
import android.widget.EditText
import com.chad.gads2022_java_kotlin.app.Constants
import com.google.android.material.textfield.TextInputLayout
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
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

        }

        //Repositories On Click Listener
        listRepositoriesButton.setOnClickListener {
            if(isNotEmpty(etRepoName, inputLayoutName)) {

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