package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private final RecipeAdapterOnClickHandler mClickHandler;
    private Context mContext;
    private Recipe[] mRecipes;

    public RecipeAdapter(Context context, RecipeAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        String name = mRecipes[position].getName();
        if (name.contentEquals("Nutella Pie")) {
            Picasso.with(mContext).load(R.drawable.nutella_pie).into(holder.listItemRecipeView);
        } else if (name.contentEquals("Brownies")) {
            Picasso.with(mContext).load(R.drawable.brownies2).into(holder.listItemRecipeView);

        } else if (name.contentEquals("Yellow Cake")) {
            Picasso.with(mContext).load(R.drawable.yellowcake).into(holder.listItemRecipeView);

        } else if (name.contentEquals("Cheesecake")) {
            Picasso.with(mContext).load(R.drawable.cheesecake).into(holder.listItemRecipeView);

        }
        holder.listItemRecipeName.setText(name);
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) {
            return 0;
        }
        return mRecipes.length;
    }

    public void setRecipeData(Recipe[] recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    //the interface that recieves onClick messages
    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe clickedRecipe);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.rv_recipes_item)
        ImageView listItemRecipeView;
        @BindView(R.id.rv_recipes_name)
        TextView listItemRecipeName;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        // This gets called by the child views during a click.
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipes[adapterPosition];
            mClickHandler.onClick(recipe);
        }

    }
}
