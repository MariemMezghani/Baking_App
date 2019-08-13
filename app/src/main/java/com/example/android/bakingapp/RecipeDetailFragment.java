package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.Widget.ListViewWidgetService;
import com.example.android.bakingapp.Widget.RecipeWidgetProvider;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utilities.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler {
    Recipe recipe;
    @BindView(R.id.rv_recipe_steps)
    RecyclerView mStepsList;
    @BindView(R.id.rv_ingredients)
    TextView ingredientsView;
    @BindView(R.id.fragment_recipe_detail)
    LinearLayout recipeDetailView;
    private StepAdapter mStepAdapter;
    private onStepClickListener mCallbacks;

    public RecipeDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        //source 6
        setHasOptionsMenu(true);
        ButterKnife.bind(this, rootView);
        final Intent intentThatStartedThisActivity = getActivity().getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("recipe")) {
                recipe = (Recipe) intentThatStartedThisActivity.getParcelableExtra("recipe");
                ingredientsView.setText(recipe.getIngredients());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                mStepsList.setLayoutManager(linearLayoutManager);
                mStepsList.setHasFixedSize(true);
                mStepAdapter = new StepAdapter(this);
                mStepAdapter.setMovieData(recipe.getSteps());
                mStepsList.setAdapter(mStepAdapter);
                getActivity().setTitle(recipe.getName());
            }
        }
        return rootView;
    }

    @Override
    public void onClick(Step step) {
        mCallbacks.onStepClicked(step);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_to_widget) {
            SharedPreferencesUtil.addRecipeToWidgetPreference(getActivity(), recipe);
            // update the widget
            //source 7
            ComponentName widget = new ComponentName(getActivity(), RecipeWidgetProvider.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
            int[] ids = appWidgetManager.getAppWidgetIds(widget);
            RecipeWidgetProvider recipeWidgetProvider = new RecipeWidgetProvider();
            recipeWidgetProvider.onUpdate(getActivity(), appWidgetManager, ids);
            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.recipe_list_ingredients);
            Toast.makeText(getActivity(), "Added ingredients of " + recipe.getName() + " to Widget.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    // make sure RecipeDetailActivity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallbacks = (onStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement onStepClickListener");
        }
    }

    //the interface that triggers the callback in the RecipeDetailActivity
    public interface onStepClickListener {
        void onStepClicked(Step step);
    }

}
