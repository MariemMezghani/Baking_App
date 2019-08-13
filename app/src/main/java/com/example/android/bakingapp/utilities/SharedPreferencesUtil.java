package com.example.android.bakingapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.bakingapp.model.Recipe;

import java.util.List;

public class SharedPreferencesUtil {
    public static final String PREF_RRECIPE = "pref_recipe";

    public static final void addRecipeToWidgetPreference(Context context, Recipe recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREF_RRECIPE, context.MODE_MULTI_PROCESS).edit();
        prefs.putString("recipe", recipe.getName());
        prefs.putString("ingredients", recipe.getIngredients());
        //source 10
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recipe.getIngredientsList().length; i++) {
            sb.append(recipe.getIngredientsList()[i]).append("!!this is a new ingredient!!");
        }
        prefs.putString("ingredients list", sb.toString());
        prefs.apply();
    }

    public static final String[] getIngredientsList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_RRECIPE, context.MODE_MULTI_PROCESS);
        String ingredientsListForWidget = sharedPreferences.getString("ingredients list", "");
        String[] ingredientsList = ingredientsListForWidget.split("!!this is a new ingredient!!");
        return ingredientsList;
    }

    public static final String setUpRecipeNameInWidget(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_RRECIPE, context.MODE_MULTI_PROCESS);
        String nameForWidget = sharedPreferences.getString("recipe", "");
        return nameForWidget;
    }

}
