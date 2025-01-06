package com.example.nutrinavigator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeFormat extends RecyclerView.Adapter<RecipeFormat.ViewHolder> {
    private List<Recipe> recipes;
    private Context context;

    public RecipeFormat(List<Recipe> recipes){
        this.recipes = recipes;
    }

    public RecipeFormat(Context context, List<Recipe> recipes){
        this.recipes = recipes;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Recipe recipe = recipes.get(position);
        holder.viewName.setText(recipe.getName());
        holder.viewIngredients.setText(recipe.getIngredients());
        holder.viewCalories.setText(String.valueOf(recipe.getCalories()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetail.class);
            intent.putExtra("name", recipe.getName());
            intent.putExtra("ingredients", recipe.getIngredients());
            intent.putExtra("calories", recipe.getCalories());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount(){
        return recipes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView viewName;
        public TextView viewIngredients;
        public TextView viewCalories;

        public ViewHolder(View item){
            super(item);
            viewName = item.findViewById(R.id.vName);
            viewIngredients = item.findViewById(R.id.vIngredients);
            viewCalories = item.findViewById(R.id.vCalories);
        }
    }

}
