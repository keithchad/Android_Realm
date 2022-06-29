package com.chad.gads2022_java_kotlin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.gads2022_java_kotlin.R;
import com.chad.gads2022_java_kotlin.adapters.DisplayAdapter;
import com.chad.gads2022_java_kotlin.app.Constants;
import com.chad.gads2022_java_kotlin.app.Util;
import com.chad.gads2022_java_kotlin.models.Repository;
import com.chad.gads2022_java_kotlin.models.SearchResponse;
import com.chad.gads2022_java_kotlin.retrofit.GithubAPIService;
import com.chad.gads2022_java_kotlin.retrofit.RetrofitClient;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class DisplayActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	private static final String TAG = DisplayActivity.class.getSimpleName();

	private DrawerLayout mDrawerLayout;
	private RecyclerView mRecyclerView;
	private DisplayAdapter mDisplayAdapter;
	private List<Repository> browsedRepositories;
	private GithubAPIService mService;
	private Realm mRealm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);

		Toolbar toolbar = findViewById(R.id.toolbar_display);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Showing Browsed Results");

		mRecyclerView = findViewById(R.id.recyclerView);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(layoutManager);

		mService = RetrofitClient.getGithubAPIService();
		mRealm = Realm.getDefaultInstance();

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		mDrawerLayout = findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle drawerToggle
				= new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
		mDrawerLayout.addDrawerListener(drawerToggle);
		drawerToggle.syncState();

		Intent intent = getIntent();
		if (intent.getIntExtra(Constants.KEY_QUERY_TYPE, -1) == Constants.SEARCH_BY_REPO) {
			String queryRepo = intent.getStringExtra(Constants.KEY_REPO_SEARCH);
			String repoLanguage = intent.getStringExtra(Constants.KEY_LANGUAGE);
			fetchRepositories(queryRepo, repoLanguage);
		} else {
			String githubUser = intent.getStringExtra(Constants.KEY_GITHUB_USER);
			fetchUserRepositories(githubUser);
		}
	}

	private void fetchUserRepositories(String githubUser) {

		mService.searchRepositoriesByUser(githubUser).enqueue(new Callback<List<Repository>>() {
			@Override
			public void onResponse(@NonNull Call<List<Repository>> call, @NonNull Response<List<Repository>> response) {
				if (response.isSuccessful()) {
					Log.i(TAG, "posts loaded from API " + response);

					if(response.body() != null) {
						browsedRepositories = response.body();
					}

					if (browsedRepositories != null && browsedRepositories.size() > 0)
						setupRecyclerView(browsedRepositories);
					else
						Util.showMessage(DisplayActivity.this, "No Items Found");

				} else {
					Log.i(TAG, "Error " + response);
					if(response.errorBody() != null ) {
						Util.showErrorMessage(DisplayActivity.this, response.errorBody());
					}
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<Repository>> call, @NonNull Throwable t) {
				Util.showMessage(DisplayActivity.this, t.getMessage());
				Log.e("error", t.getMessage());
				Toast.makeText(DisplayActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void fetchRepositories(String queryRepo, String repoLanguage) {

		Map<String, String> query = new HashMap<>();

		if (repoLanguage != null && !repoLanguage.isEmpty())
			queryRepo += " language:" + repoLanguage;
		query.put("q", queryRepo);

		mService.searchRepositories(query).enqueue(new Callback<SearchResponse>() {
			@Override
			public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
				if (response.isSuccessful()) {
					Log.i(TAG, "posts loaded from API " + response);

					if(response.body() != null) {
						browsedRepositories = response.body().getItems();
					}

					if (browsedRepositories.size() > 0)
						setupRecyclerView(browsedRepositories);
					else
						Util.showMessage(DisplayActivity.this, "No Items Found");

				} else {
					Log.i(TAG, "error " + response);
					if(response.errorBody() != null ) {
						Util.showErrorMessage(DisplayActivity.this, response.errorBody());
					}

				}
			}

			@Override
			public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
				Util.showMessage(DisplayActivity.this, t.toString());
			}
		});
	}

	private void setupRecyclerView(List<Repository> items) {
		mDisplayAdapter = new DisplayAdapter(this, items);
		mRecyclerView.setAdapter(mDisplayAdapter);
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

		menuItem.setChecked(true);
		closeDrawer();

		switch (menuItem.getItemId()) {

			case R.id.item_bookmark:
				showBookmarks();
				getSupportActionBar().setTitle("Showing Bookmarks");
				break;

			case R.id.item_browsed_results:
				showBrowsedResults();
				getSupportActionBar().setTitle("Showing Browsed Results");
				break;
		}

		return true;
	}

	private void showBrowsedResults() {
		mDisplayAdapter.swap(browsedRepositories);
	}

	private void showBookmarks() {
		mRealm.executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(@NonNull Realm realm) {
				RealmResults<Repository> repositories = realm.where(Repository.class).findAll();
				mDisplayAdapter.swap(repositories);
			}
		});
	}

	private void closeDrawer() {
		mDrawerLayout.closeDrawer(GravityCompat.START);
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
			closeDrawer();
		else {
			super.onBackPressed();
			mRealm.close();
		}
	}
}
