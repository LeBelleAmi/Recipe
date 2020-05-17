package com.lebelleami.recipe.Controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.lebelleami.recipe.Model.Recipe;
import com.lebelleami.recipe.R;


public class StepFragment extends Fragment {

    View view;

    ImageView stepThumbnail;
    String stepTitle, stepDesc, stepVideoUrl, stepThumbnailUrl;
    boolean playWhenReady = true;
    long playbackPosition;
    int currentWindow, stepId, stepNo, stepNum, stepPosition;
    TextView stepTitleText, stepDescText, stepNumber;
    PlayerView exoPlayerView;
    SimpleExoPlayer player;

    Recipe recipe;



    public StepFragment() {
        // Required empty public constructor
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.step_fragment, container, false);

        //get items from the adapter intent and set in views
        Bundle data = getActivity().getIntent().getBundleExtra("steps");
        stepTitle = data.getString("stepTitle");
        stepDesc = data.getString("stepDesc");
        stepVideoUrl = data.getString("stepVideo");
        stepThumbnailUrl = data.getString("stepThumb");
        stepId = getActivity().getIntent().getIntExtra("pos", 0);
        stepNo = getActivity().getIntent().getIntExtra("stepno", 0);
        stepPosition = getActivity().getIntent().getIntExtra("position", 0);

        stepNum = stepNo - 1;



        stepTitleText = view.findViewById(R.id.tv_step_title);
        stepDescText = view.findViewById(R.id.tv_step_description);
        //stepNumber = view.findViewById(R.id.tv_page);
        stepThumbnail = view.findViewById(R.id.noVideoImageView);
        exoPlayerView = view.findViewById(R.id.playerView);


        //set step details info
        stepTitleText.setText(stepTitle);
        stepDescText.setText(stepDesc);



        if(stepThumbnailUrl != null && !stepThumbnailUrl.isEmpty()){
            stepThumbnail.setVisibility(View.VISIBLE);
            Glide.with(this).load(stepThumbnailUrl).into(stepThumbnail);

        }else {
            stepThumbnail.setVisibility(View.GONE);
        }


        if (stepVideoUrl != null && !stepVideoUrl.isEmpty()) {

            // Init and show video view
            exoPlayerView.setVisibility(View.VISIBLE);

        } else {
            // Hide video view
            exoPlayerView.setVisibility(View.GONE);
        }

        return view;
    }


    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
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


}
