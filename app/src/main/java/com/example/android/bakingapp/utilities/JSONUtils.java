package com.example.android.bakingapp.utilities;

import android.util.Log;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JSONUtils {
    private static final String TAG = "CREATION";

    public static Recipe[] parseRecipeJson(String json) {
        try {
            //convert json string to json array
            JSONArray recipeJsonArray = new JSONArray(json);
            Recipe[] recipes = new Recipe[recipeJsonArray.length()];
            //looping through all recipes
            for (int i = 0; i < recipeJsonArray.length(); i++) {
                JSONObject c = recipeJsonArray.getJSONObject(i);
                int id = c.getInt("id");
                String name = c.getString("name");
                String ingredients = "";
                JSONArray ingredientsArray = c.getJSONArray("ingredients");
                String[] ingredientsList= new String[ingredientsArray.length()] ;
                for (int x = 0; x < ingredientsArray.length(); x++) {
                    JSONObject d = ingredientsArray.getJSONObject(x);
                    String quantity = d.getString("quantity");
                    String measure = d.getString("measure");
                    String ingredient = d.getString("ingredient");
                    ingredients = ingredients + " - " + quantity + " " + measure + " " + ingredient + "\n";
                    ingredientsList[x] = quantity + " " + measure + " " + ingredient;
                }
                JSONArray stepsArray = c.getJSONArray("steps");
                Step[] steps = new Step[stepsArray.length()];
                //looping through all steps
                for (int w = 0; w < stepsArray.length(); w++) {
                    JSONObject e = stepsArray.getJSONObject(w);
                    int idNr = e.getInt("id");
                    String shortDescription = e.getString("shortDescription");
                    String videoUrl = e.getString("videoURL");
                    String description = e.getString("description");
                    String thumbnailURL = e.getString("thumbnailURL");
                    Step step = new Step(idNr, shortDescription, videoUrl, description, thumbnailURL);
                    steps[w] = step;
                }
                Recipe recipe = new Recipe(id, name, ingredients, steps,ingredientsList);
                recipes[i] = recipe;
            }
            return recipes;

        } catch (JSONException e) {
            e.printStackTrace();
            Log.wtf(TAG, "JSON error");

            return null;
        }
    }
}
