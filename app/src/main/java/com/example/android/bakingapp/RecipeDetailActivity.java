package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.onStepClickListener {
    Menu menu;
    Recipe recipe;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        final Intent intentThatStartedThisActivity = this.getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("recipe")) {
                recipe = (Recipe) intentThatStartedThisActivity.getParcelableExtra("recipe");
            }
        }
        if (findViewById(R.id.recipe_detail_tablet)!= null){
            mTwoPane=true;
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, recipeDetailFragment)
                    .commit();

            if (savedInstanceState== null) {
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.isTablet(true);
                stepDetailFragment.setRecipe(recipe);
                stepDetailFragment.setStep(recipe.getSteps()[0]);
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, stepDetailFragment)
                        .commit();
            }
        }else{
            mTwoPane=false;
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, recipeDetailFragment)
                    .commit();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }


    @Override
    public void onStepClicked(Step step) {
        if (mTwoPane){
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setRecipe(recipe);
            stepDetailFragment.setStep(step);
            stepDetailFragment.isTablet(true);
            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_container, stepDetailFragment)
                    .commit();
        }else{
            Context context = this;
            Class destinationClass = StepDetailActivity.class;
            Intent intentToStartDetailActivity = new Intent(context, destinationClass);
            intentToStartDetailActivity.putExtra("step", step);
            intentToStartDetailActivity.putExtra("recipe", recipe);
            startActivity(intentToStartDetailActivity);
        }
        }

    }

