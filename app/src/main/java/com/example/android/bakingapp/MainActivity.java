package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utilities.JSONUtils;
import com.example.android.bakingapp.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Recipe[]>
        , RecipeAdapter.RecipeAdapterOnClickHandler {
    private static final String TAG = "CREATION";
    private static final int RECIPE_LOADER_ID = 24;
    //source1+2
    @BindView(R.id.rv_recipes)
    RecyclerView mRecipesList;
    @BindView(R.id.error_message)
    TextView mErrorMessage;
    private RecipeAdapter mRecipeAdapter;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (findViewById(R.id.main_activity_tablet) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
        if (mTwoPane == true) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            mRecipesList.setLayoutManager(gridLayoutManager);
        } else if (mTwoPane == false) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecipesList.setLayoutManager(linearLayoutManager);
        }
        mRecipesList.setHasFixedSize(true);
        if (NetworkUtils.isOnline(this)) {
            mRecipeAdapter = new RecipeAdapter(this, this);
            mRecipesList.setAdapter(mRecipeAdapter);
            getSupportLoaderManager().initLoader(RECIPE_LOADER_ID, null, this);

        } else {
            showErrorMessage();
        }
    }

    //show error message and hide the recyclerview
    private void showErrorMessage() {
        mRecipesList.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Recipe recipe) {
        Context context = this;
        Class destinationClass = RecipeDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("recipe", recipe);
        startActivity(intentToStartDetailActivity);
    }

    @NonNull
    @Override
    public Loader<Recipe[]> onCreateLoader(int i, @Nullable Bundle bundle) {

        return new AsyncTaskLoader<Recipe[]>(this) {
            @Override
            public void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Nullable
            @Override
            public Recipe[] loadInBackground() {
                URL url = NetworkUtils.buildURL();
                Recipe[] recipes_list;
                String results = null;
                try {
                    results = NetworkUtils.getResponseFromHttpUrl(url);
                    recipes_list = JSONUtils.parseRecipeJson(results);
                    return recipes_list;

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "error");
                    return null;

                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Recipe[]> loader, Recipe[] recipes_list) {
        if (recipes_list != null) {
            mRecipeAdapter.setRecipeData(recipes_list);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Recipe[]> loader) {

    }

}
