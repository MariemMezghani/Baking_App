package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {

    //creating new objects, individually or as arrays
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {

        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }

    };

    private int id;
    private String name;
    private String ingredients;
    private Step[] steps;
    private String[] ingredientsList;

    public Recipe(int id, String name, String ingredients, Step[] steps, String[] ingredientsList) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.ingredientsList = ingredientsList;
    }

    //reconstructing user object from parcel
    public Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredients = in.readString();
        //source4
        steps = in.createTypedArray(Step.CREATOR);
        ingredientsList=in.createStringArray();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //object serialization
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(ingredients);
        //resource4
        dest.writeTypedArray(steps, 0);
        dest.writeStringArray(ingredientsList);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public Step[] getSteps() {
        return steps;
    }

    public String[] getIngredientsList(){return ingredientsList;}
}
