package com.chad.gads2022_java_kotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chad.gads2022_java_kotlin.R
import io.realm.RealmConfiguration
import android.content.Intent
import com.chad.gads2022_java_kotlin.app.Constants
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeRealm()
        initialize()
    }

    //Initialize Views
    private fun initialize() {
        setSupportActionBar(toolbar_main)

        saveNameButton.setOnClickListener {

        }

        listRepositoriesButton.setOnClickListener {
            val repoName = etRepoName.text.toString()
            val language = etLanguage.text.toString()

            val intent = Intent(this@MainActivity, DisplayActivity::class.java)
            intent.putExtra(Constants.KEY_QUERY_TYPE, Constants.SEARCH_BY_REPO)
            intent.putExtra(Constants.KEY_REPO_SEARCH, repoName)
            intent.putExtra(Constants.KEY_LANGUAGE, language)
            startActivity(intent)

        }

        listUserRepositoriesButton.setOnClickListener {
            val githubUser = etGithubUser.text.toString()

            val intent = Intent(this@MainActivity, DisplayActivity::class.java)
            intent.putExtra(Constants.KEY_QUERY_TYPE, Constants.SEARCH_BY_USER)
            intent.putExtra(Constants.KEY_GITHUB_USER, githubUser)
            startActivity(intent)
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