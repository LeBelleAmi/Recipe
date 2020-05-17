package com.lebelleami.recipe.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.lebelleami.recipe.Model.Ingredient;
import com.lebelleami.recipe.Model.Recipe;
import com.lebelleami.recipe.Model.Step;
import com.lebelleami.recipe.R;
import com.lebelleami.recipe.View.IngredientsAdapter;
import com.lebelleami.recipe.View.RecipeAdapter;
import com.lebelleami.recipe.View.StepAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipesDetailsActivity extends AppCompatActivity {

    ImageView backdropImg;
    TextView recipeText, recipeServing, recipeTimer;
    RecyclerView ingredientsRecyclerView, stepsRecyclerView;
    //recipe ingredients,will map an arraylist of ingredients
    ArrayList<Ingredient> ingredients;
    ArrayList<Step> steps;
    Recipe recipe;
    String recipeTitle, recipeSteps, recipeImage, recipeIngredients;
    int recipeServe, mPostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        backdropImg = findViewById(R.id.backdrop);
        recipeText = findViewById(R.id.recipeTitleTwo);
        recipeServing = findViewById(R.id.recipeServingTwo);
        recipeTimer = findViewById(R.id.recipeTimeTwo);
        ingredientsRecyclerView = findViewById(R.id.ingredients_list);
        stepsRecyclerView = findViewById(R.id.steps_list);



        //get items from the adapter intent and set in views
        Bundle data = getIntent().getBundleExtra("recipes");
        recipe = data.getParcelable("Recipe");
        recipeTitle = recipe.getName();
        recipeServe = recipe.getServings();
        recipeImage = recipe.getImage();
        mPostId = recipe.getId();
        //mPostId = getIntent().getIntExtra("pos", 0);
        ingredients = data.getParcelableArrayList("ingredientsList");
        steps = data.getParcelableArrayList("stepsList");


        //set recipe details info
        recipeText.setText(recipeTitle);
        recipeServing.setText(recipeServe + " Servings");

        switch (mPostId){
            case 1 : backdropImg.setImageResource(R.drawable.nutella);
                recipeTimer.setText(R.string.nutellatime);
                break;
            case 2 : backdropImg.setImageResource(R.drawable.brownies);
                recipeTimer.setText(R.string.browniestime);
                break;
            case 3 : backdropImg.setImageResource(R.drawable.yellowcake);
                recipeTimer.setText(R.string.yellowcaketime);
                break;
            case 4 : backdropImg.setImageResource(R.drawable.cheesecake);
                recipeTimer.setText(R.string.cheesecaketime);
                break;
        }

        initializeIngredients();
        initializeSteps();
        initCollapsingToolbar();

    }



    private void initializeIngredients(){
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(this, ingredients);
       ingredientsRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

    }

    private void initializeSteps() {
        StepAdapter stepAdapter = new StepAdapter(this, steps);
        stepsRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stepsRecyclerView.setLayoutManager(layoutManager);
        stepsRecyclerView.setAdapter(stepAdapter);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, layoutManager.getOrientation());
        stepsRecyclerView.addItemDecoration(dividerItemDecoration);


    }


    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(recipeTitle);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            //navigateUpFromSameTask(this);
            onBackPressed(); // close this activity and return to preview activity (if there is any)
            //return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
