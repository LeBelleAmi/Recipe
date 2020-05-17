package com.lebelleami.recipe.View;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lebelleami.recipe.Model.Ingredient;
import com.lebelleami.recipe.R;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>{
    private Context context;
    private List<Ingredient> ingredients;


    public IngredientsAdapter(Context applicationContext, List<Ingredient> mIngredients)
    {
        this.ingredients = mIngredients;
        this.context = applicationContext;
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public IngredientsAdapter.IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredient_list_item, viewGroup, false);
        IngredientsAdapter.IngredientsViewHolder ingredientsViewHolder = new IngredientsAdapter.IngredientsViewHolder(view);
        return ingredientsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final IngredientsAdapter.IngredientsViewHolder ingredientsViewHolder, final int i) {
        Ingredient ingredient = ingredients.get(i);
        ingredientsViewHolder.ingredient_Title.setText(ingredient.getIngredient());
        String quantity = String.valueOf(ingredient.getQuantity());
        ingredientsViewHolder.quantity_text.setText(quantity);
        ingredientsViewHolder.measure_text.setText(ingredient.getMeasure());

    }


    @Override
    public int getItemCount() {

        if (ingredients == null) {
            return 0;
        } else {
            return ingredients.size();
        }
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        TextView ingredient_Title;
        TextView quantity_text;
        TextView measure_text;


        public IngredientsViewHolder (View itemView){
            super(itemView);
            ingredient_Title = itemView.findViewById(R.id.ingredient_text);
            quantity_text = itemView.findViewById(R.id.quantity_text);
            measure_text = itemView.findViewById(R.id.measure_text);
        }


    }}

