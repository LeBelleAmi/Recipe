package com.lebelleami.recipe.Controllers;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.lebelleami.recipe.Model.Recipe;
import com.lebelleami.recipe.Model.Step;
import com.lebelleami.recipe.R;

public class StepsActivity extends AppCompatActivity {

    Step step;
    Button prevButton, nextButton;
    ImageView stepThumbnail;
    String stepTitle, stepDesc, stepVideoUrl, stepThumbnailUrl;
    boolean playWhenReady = true;
    long playbackPosition;
    int currentWindow, stepId, stepNo, stepNum, stepPosition;
    TextView stepTitleText, stepDescText;
    PlayerView exoPlayerView;
    SimpleExoPlayer player;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        progressBar = findViewById(R.id.circular_progress);


        //get items from the adapter intent and set in views
        Bundle data = getIntent().getBundleExtra("steps");
        step = data.getParcelable("Step");
        stepTitle = step.getShortDescription();
        stepDesc = step.getDescription();
        stepVideoUrl = step.getVideoURL();
        stepThumbnailUrl = step.getThumbnailURL();
        stepId = step.getId();
        stepNo = getIntent().getIntExtra("stepno", 0);
        stepPosition = getIntent().getIntExtra("position", 0);

        stepNum = stepNo - 1;

        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(stepTitle);



        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //prevButton = findViewById(R.id.btn_prev);
        //nextButton = findViewById(R.id.btn_next);
        stepTitleText = findViewById(R.id.tv_step_title);
        stepDescText = findViewById(R.id.tv_step_description);
        //stepNumber = findViewById(R.id.tv_page);
        stepThumbnail = findViewById(R.id.noVideoImageView);
        exoPlayerView = findViewById(R.id.playerView);


        //set step details info
        stepTitleText.setText(stepTitle);
       stepDescText.setText(stepDesc);



        if (stepThumbnailUrl.isEmpty() || stepThumbnailUrl.contains(
                stepThumbnail.getContext().getString(R.string.mp4))) {
            // Hide ImageView thumbnail
            stepThumbnail.setVisibility(View.GONE);
        } else{
            stepThumbnail.setVisibility(View.VISIBLE);
            Glide.with(this).load(stepThumbnailUrl).into(stepThumbnail);
        }

       /*if(stepThumbnailUrl != null && !stepThumbnailUrl.isEmpty()){
            stepThumbnail.setVisibility(View.VISIBLE);
            Glide.with(this).load(stepThumbnailUrl).into(stepThumbnail);

        }else {
            stepThumbnail.setVisibility(View.GONE);
        }
*/

        if (stepVideoUrl != null && !stepVideoUrl.isEmpty()) {

            // Init and show video view
            exoPlayerView.setVisibility(View.VISIBLE);

        } else {
            // Hide video view
            exoPlayerView.setVisibility(View.GONE);
        }


        //onPrevButtonClick();
        //onNextButtonClick();

    }




    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        exoPlayerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(stepVideoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);

    }


    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
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


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        exoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }


   /* private void setupBottomBar() {
        int maxStepNumber = stepNum;

        if (stepId == 0) {
            prevButton.setEnabled(false);
            prevButton.setAlpha(0.3f);

        } else if (0 < stepId && stepId < (maxStepNumber)) {
            prevButton.setEnabled(true);
            nextButton.setEnabled(true);
            prevButton.setAlpha(1f);
            nextButton.setAlpha(1f);
        } else if (stepId >= maxStepNumber) {
            nextButton.setEnabled(false);
            nextButton.setAlpha(0.3f);
        }

        stepNumber.setText("Step " + stepId + " of " + maxStepNumber);

    }
*/

    /*private void replaceStep(){
        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("clickedPosition", stepId);
        stepFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_fragment_container, stepFragment)
                .addToBackStack(null)
                .commit();
    }*/
/*
    public void onPrevButtonClick(){
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stepId--;
                setupBottomBar();
                //replaceStep();
                stepTitleText.setText(step.getShortDescription());
                stepDescText.setText(step.getDescription());
           }
        });
    }

    public void onNextButtonClick(){
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepId++;
                setupBottomBar();
                //replaceStep();
                step = mRecipe.getSteps().get(stepId);
                stepTitleText.setText(step.getShortDescription());
                stepDescText.setText(step.getDescription());
            }
        });

    }*/
}
