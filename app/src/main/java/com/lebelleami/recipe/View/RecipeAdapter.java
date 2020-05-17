package com.lebelleami.recipe.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lebelleami.recipe.Model.Ingredient;
import com.lebelleami.recipe.Model.Recipe;
import com.lebelleami.recipe.Model.Step;
import com.lebelleami.recipe.R;
import com.lebelleami.recipe.Controllers.RecipesDetailsActivity;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{
    private Context context;
    private ArrayList<Recipe> recipeList;


    public RecipeAdapter (Context applicationContext, ArrayList<Recipe> recipeArrayList) {
        this.context = applicationContext;
        this.recipeList = recipeArrayList;
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_content, viewGroup, false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view);
        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeAdapter.RecipeViewHolder recipeViewHolder, final int i) {
        Recipe recipe = recipeList.get(i);
        recipeViewHolder.recipe_Title.setText(recipe.getName());
        final String serving = String.valueOf(recipe.getServings());
        //int serving = recipe.getServings();
        //String numInString = String.valueOf(serving);
        recipeViewHolder.recipe_Serving.setText(serving + " Servings");


        switch (i){
            case 0 : recipeViewHolder.food_Image.setImageResource(R.drawable.nutella);
            recipeViewHolder.recipe_Time.setText(R.string.nutellatime);
                break;
            case 1 : recipeViewHolder.food_Image.setImageResource(R.drawable.brownies);
                recipeViewHolder.recipe_Time.setText(R.string.browniestime);
                break;
            case 2 : recipeViewHolder.food_Image.setImageResource(R.drawable.yellowcake);
                recipeViewHolder.recipe_Time.setText(R.string.yellowcaketime);
                break;
            case 3 : recipeViewHolder.food_Image.setImageResource(R.drawable.cheesecake);
                recipeViewHolder.recipe_Time.setText(R.string.cheesecaketime);
                break;
        }

        recipeViewHolder.recipe_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "Testy", Toast.LENGTH_SHORT).show();
                int pos = recipeViewHolder.getAdapterPosition();
                Recipe recipes = recipeList.get(pos);

                int id = recipeList.get(pos).getId();
                ArrayList<Ingredient> ingredients = new ArrayList<>(recipeList.get(pos).getIngredients());
                ArrayList<Step> steps = new ArrayList<>(recipeList.get(pos).getSteps());


                Bundle args = new Bundle();
                args.putParcelableArrayList("ingredientsList", ingredients);
                args.putParcelableArrayList("stepsList", steps);
                args.putParcelable("Recipe", recipes);


                Intent intent = new Intent(context, RecipesDetailsActivity.class);
                intent.putExtra("recipes", args);
                intent.putExtra("pos", id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {

        if (recipeList == null) {
            return 0;
        } else {
            return recipeList.size();
        }
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder {



        CardView cardView;
        ImageView food_Image;
        TextView recipe_Title;
        TextView recipe_Serving;
        TextView recipe_Time;
        Button recipe_Button;


        public RecipeViewHolder (View itemView){
            super(itemView);


            cardView = itemView.findViewById(R.id.recipeCard);
            food_Image = itemView.findViewById(R.id.food_image);
            recipe_Title = itemView.findViewById(R.id.recipeTitle);
            recipe_Serving = itemView.findViewById(R.id.recipeServing);
            recipe_Time = itemView.findViewById(R.id.recipeTime);
            recipe_Button = itemView.findViewById(R.id.recipeButton);

    }


        }



}
