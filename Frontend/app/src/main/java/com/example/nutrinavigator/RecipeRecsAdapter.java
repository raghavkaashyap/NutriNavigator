package com.example.nutrinavigator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public abstract class RecipeRecsAdapter extends RecyclerView.Adapter<RecipeRecsAdapter.RecipeViewHolder> {
    private List<Recipe> recipes = Collections.emptyList();
    private RecListener listener = null;

    public RecipeRecsAdapter() {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reciperec, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.tvRecipeName.setText(recipe.getName());
        //holder.tvRecipeDescription.setText(recipe.getDescription());

       // holder.btnLike.setOnClickListener(v -> listener.onLike(recipe.getName()));
        //holder.btnDislike.setOnClickListener(v -> listener.onDislike(recipe.getName()));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public abstract void onLike(int recipeId);

    public abstract void onDislike(int recipeId);

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeName, tvRecipeDescription;
        Button btnLike, btnDislike;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvRecipeDescription = itemView.findViewById(R.id.tvRecipeDescription);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnDislike = itemView.findViewById(R.id.btnDislike);
        }
    }
}

