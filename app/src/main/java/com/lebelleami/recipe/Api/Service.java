package com.lebelleami.recipe.Api;

import com.lebelleami.recipe.Model.Recipe;
import com.lebelleami.recipe.Utils.Url;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
    //Get list of recipes
    @GET(Url.END_POINT)
    Call<ArrayList<Recipe>> getRecipes();
}
