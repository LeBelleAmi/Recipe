package com.lebelleami.recipe.Controllers;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lebelleami.recipe.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        /*
         * Declare name of font used for pillbox
         */

        TextView textView = findViewById(R.id.pastry_text);
        ImageView imageView = findViewById(R.id.pastry_icon);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.transitions);
        textView.startAnimation(animation);
        //imageView.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent startActivity = new Intent(MainActivity.this, RecipesListActivity.class);
                startActivity(startActivity);
                finish();
            }
        }, 3000);
    }
}
