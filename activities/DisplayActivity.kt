package com.chad.gads2022_java_kotlin.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.gads2022_java_kotlin.R
import com.chad.gads2022_java_kotlin.adapters.DisplayAdapter
import com.chad.gads2022_java_kotlin.app.Constants
import com.chad.gads2022_java_kotlin.app.Util
import com.chad.gads2022_java_kotlin.models.Repository
import com.chad.gads2022_java_kotlin.models.SearchResponse
import com.chad.gads2022_java_kotlin.retrofit.GithubAPIService
import com.chad.gads2022_java_kotlin.retrofit.RetrofitClient
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_display.*
import kotlinx.android.synthetic.main.header.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisplayActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var displayAdapter: DisplayAdapter
    private var repository: List<Repository> =  mutableListOf()
    private val githubAPIService: GithubAPIService by lazy {
        RetrofitClient.getGithubAPIService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        //Initialize
        initialize()

        //Get Bundle Data
        bundleData()
    }

    //Initialize Views
    private fun initialize() {

        //Initialize Toolbar and set title
        setSupportActionBar(toolbar_display)
        supportActionBar!!.title = "Showing Browsed Results"

        //Initialize RecyclerView and set LayoutManager
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        //Initialize NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        //Initialize DrawerLayout
        val drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar_display,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

    //Get Intent Data and assign to variables
    private fun bundleData() {
        val intent = intent
        if (intent.getIntExtra(Constants.KEY_QUERY_TYPE, -1) == Constants.SEARCH_BY_REPO) {
            val queryRepo = intent.getStringExtra(Constants.KEY_REPO_SEARCH)
            val repoLanguage = intent.getStringExtra(Constants.KEY_LANGUAGE)
            fetchRepositories(queryRepo, repoLanguage)
        } else {
            val githubUser = intent.getStringExtra(Constants.KEY_GITHUB_USER)
            fetchUserRepositories(githubUser)
        }

        setAppUserName()
    }

    private fun setAppUserName() {

        val sharedPreferences = getSharedPreferences(Constants.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val name = sharedPreferences.getString(Constants.KEY_PERSON_NAME, "User")
        val headerView = navigationView.getHeaderView(0)
        headerView.txvName.text = name

    }

    //Fetch User Repositories from Github Api
    private fun fetchUserRepositories(githubUser: String?) {
        githubAPIService.searchRepositoriesByUser(githubUser).enqueue(object : Callback<List<Repository>>{
            override fun onResponse(
                call: Call<List<Repository>>,
                response: Response<List<Repository>>
            ) {
               if (response.isSuccessful) {
                   Log.i(TAG, "Posts from API")

                   response.body() ?. let {
                       repository = it
                   }

                   if (repository.isNotEmpty()) {
                       setupRecyclerView(repository)
                   } else {
                       Util.showMessage(this@DisplayActivity, "No items found")
                   }

               } else {
                   Log.i(TAG, "Error $response")
                   Util.showErrorMessage(this@DisplayActivity, response.errorBody())
               }
            }

            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                Util.showMessage(this@DisplayActivity, t.message)
            }
        })
    }

    //Fetch Repositories from Github Api
    private fun fetchRepositories(queryRepo: String?, repoLanguage: String?) {
        var queryRepo = queryRepo
        val query: MutableMap<String, String?> = HashMap()
        if (repoLanguage != null && repoLanguage.isNotEmpty()) queryRepo += " language:$repoLanguage"
        query["q"] = queryRepo
        githubAPIService.searchRepositories(query).enqueue(object : Callback<SearchResponse?> {
            override fun onResponse(
                call: Call<SearchResponse?>,
                response: Response<SearchResponse?>
            ) {
                if (response.isSuccessful) {
                    Log.i(TAG, "posts loaded from API $response")

                    response.body()?.items?.let {
                        repository = it
                    }

                    if (repository.isNotEmpty()) setupRecyclerView(repository) else Util.showMessage(
                        this@DisplayActivity,
                        "No Items Found"
                    )
                } else {
                    Log.i(TAG, "error $response")
                    if (response.errorBody() != null) {
                        Util.showErrorMessage(this@DisplayActivity, response.errorBody())
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse?>, t: Throwable) {
                Util.showMessage(this@DisplayActivity, t.toString())
            }
        })
    }

    //Setup RecyclerView
    private fun setupRecyclerView(items: List<Repository>) {
        displayAdapter = DisplayAdapter(this, items)
        recyclerView.adapter = displayAdapter
    }

    //Setup Drawer
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        menuItem.isChecked = true
        closeDrawer()
        when (menuItem.itemId) {
            R.id.item_bookmark -> {
                showBookmarks()
                supportActionBar!!.title = "Showing Bookmarks"
            }
            R.id.item_browsed_results -> {
                showBrowsedResults()
                supportActionBar!!.title = "Showing Browsed Results"
            }
        }
        return true
    }

    //Swap to Repositories
    private fun showBrowsedResults() {
        displayAdapter.swap(repository)
    }

    //Swap to Bookmarks
    private fun showBookmarks() {
    }

    //Close Drawer
    private fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    //Back Pressed
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) closeDrawer() else {
            super.onBackPressed()
        }
    }

    //Companion Object
    companion object {
        private val TAG = DisplayActivity::class.java.simpleName
    }
}