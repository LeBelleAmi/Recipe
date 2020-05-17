package com.lebelleami.recipe.Controllers;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lebelleami.recipe.Api.Client;
import com.lebelleami.recipe.Api.Service;
import com.lebelleami.recipe.Model.Recipe;
import com.lebelleami.recipe.R;
import com.lebelleami.recipe.Utils.ViewAnimation;
import com.lebelleami.recipe.View.RecipeAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesListActivity extends AppCompatActivity {

    /*network state boolean*/
    private boolean connected;

    private final static int LOADING_DURATION = 5000;

    /*recycler view*/
    RecyclerView recyclerView;

    /*recycler view layout manager*/
    LinearLayoutManager llm;
    Recipe recipe;
    RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> recipeArrayList = new ArrayList<>();



    //app main layout
    RelativeLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        coordinatorLayout = findViewById(R.id.mainLayout);
        recyclerView = findViewById(R.id.item_list);

        connected();
        loadingAndDisplayContent();
    }


    //progress bar

    private void loadingAndDisplayContent() {
        final LinearLayout lyt_progress = findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);
        recyclerView.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewAnimation.fadeOut(lyt_progress);
            }
        }, LOADING_DURATION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initViews();
            }
        }, LOADING_DURATION + 400);
    }



    public void initViews() {
        recyclerView.setVisibility(View.VISIBLE);
        // set up recycler view
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(recipeAdapter);
    }

    private void loadRecipeDATA(){
        try{
            Service apiService =
                    Client.getClient().create(Service.class);
            Call<ArrayList<Recipe>> call = apiService.getRecipes();
            call.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                    if (response.isSuccessful()) {

                        //Log.i(TAG, "movies: " + response.body().getResults().toString());
                        //Toast.makeText(getActivity().getApplicationContext(), response.body().toString() + "string", Toast.LENGTH_LONG).show();
                        recipeArrayList = response.body();
                        //Toast.makeText(getApplicationContext(), response.body() + "loaded", Toast.LENGTH_LONG).show();

                        recipeAdapter = new RecipeAdapter(getApplicationContext(), recipeArrayList);
                        recyclerView.smoothScrollToPosition(0);
                        recyclerView.setAdapter(recipeAdapter);
                        recipeAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    // showing snack bar with response failure option
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Network error occured, please check your network and refresh!", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("REFRESH", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // refresh is selected, refresh the app
                            loadRecipeDATA();
                        }
                    });
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    snackbar.show();
                }
            });



        }catch (Exception e) {
            Log.d("Error", e.getMessage());

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }



    private void connected(){
        connected = isNetworkConnected(getApplicationContext());
        if (!connected){
            // showing snack bar with network option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "You seem to be Offline, please check connection!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // retry is selected, refresh the app
                    loadRecipeDATA();
                }
            });
            snackbar.setActionTextColor(Color.WHITE);

            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.logo_color));
            snackbar.show();
        }else {
            loadRecipeDATA();
        }
    }


    public static boolean isNetworkConnected(Context context) {
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = true;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        result = true;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }
}
