package com.example.android.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepDetailFragment extends Fragment {
    private final static String VIDEO_POSITION = "video position";
    private final static String VIDEO_STATUS = "video status";
    private static final String SAVED_RECIPE = "saved recipe";
    private static final String SAVED_STEP = "saved mStep";
    int stepId;
    Step[] stepsList;
    @BindView(R.id.fragment_step_detail)
    LinearLayout stepDetailView;
    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.step_description_view)
    TextView mDescriptionView;
    @BindView(R.id.no_video)
    ImageView mNoVideoImage;
    @BindView(R.id.back_button)
    ImageView backButton;
    @BindView(R.id.next_button)
    ImageView nextButton;
    @BindView(R.id.step_number)
    TextView stepNumber;
    private SimpleExoPlayer mExoplayer;
    private Boolean videoAvailable;
    private long videoPosition;
    private Boolean playWhenReady;
    private String videoURL;
    private boolean isTwoPane;
    private Step mStep;
    private Recipe recipe;

    //constructor
    public StepDetailFragment() {
    }

    //setters
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setStep(Step step) {
        this.mStep = step;
    }

    public void isTablet(Boolean isTwoPane) {
        this.isTwoPane = isTwoPane;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, rootView);
        final Intent intentThatStartedThisActivity = getActivity().getIntent();
        if (isTwoPane) {
            stepId = mStep.getId();
            stepsList = recipe.getSteps();
            if (savedInstanceState != null) {
                videoPosition = savedInstanceState.getLong(VIDEO_POSITION);
                playWhenReady = savedInstanceState.getBoolean(VIDEO_STATUS);

            } else {
                videoPosition = 0;
                playWhenReady = true;
            }

            setUpFragmentView(stepId);
        } else {

            if (savedInstanceState != null) {
                recipe = savedInstanceState.getParcelable(SAVED_RECIPE);
                mStep = savedInstanceState.getParcelable(SAVED_STEP);
                videoPosition = savedInstanceState.getLong(VIDEO_POSITION);
                playWhenReady = savedInstanceState.getBoolean(VIDEO_STATUS);
            } else {
                recipe = intentThatStartedThisActivity.getParcelableExtra("recipe");
                mStep = (Step) intentThatStartedThisActivity.getParcelableExtra("step");
                videoPosition = 0;
                playWhenReady = true;
            }
            stepsList = recipe.getSteps();
            stepId = mStep.getId();
            setUpFragmentView(stepId);
        }
        return rootView;
    }

    public void setUpFragmentView(final int stepId) {
        mStep = stepsList[stepId];
        videoURL = mStep.getVideoURL();
        String thumbnailURL = mStep.getThumbnailURL();
        String description = mStep.getDescription();
        mDescriptionView.setText(description);
        if (!videoURL.isEmpty()) {
            videoAvailable = true;
            mPlayerView.setVisibility(View.VISIBLE);
            mNoVideoImage.setVisibility(View.GONE);
            initializePlayer(Uri.parse(videoURL));
        } else if (thumbnailURL.contains(".mp4")) {
            videoAvailable = true;
            mNoVideoImage.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(thumbnailURL));
        } else if (!thumbnailURL.isEmpty()) {

            Picasso.with(getActivity()).load(thumbnailURL).into(mNoVideoImage);

        } else {
            videoAvailable = false;
            mNoVideoImage.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(R.drawable.no_video).into(mNoVideoImage);
            mPlayerView.setVisibility(View.GONE);
        }
        getActivity().setTitle("Step" + mStep.getId());
        if (stepId == stepsList.length - 1) {
            nextButton.setVisibility(View.INVISIBLE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }
        if (stepId == 0) {
            backButton.setVisibility(View.INVISIBLE);
        } else {
            backButton.setVisibility(View.VISIBLE);
        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoAvailable) {
                    releasePlayer();
                }
                videoPosition = 0;
                setUpFragmentView(stepId + 1);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoAvailable) {
                    releasePlayer();
                }
                videoPosition = 0;
                setUpFragmentView(stepId - 1);
            }
        });
        stepNumber.setText(stepId + "/" + (stepsList.length - 1));
    }

    public void initializePlayer(Uri uri) {
        if (mExoplayer == null) {
            //create an instance of the Exoplayer
            TrackSelector trackselector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoplayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackselector, loadControl);
            mPlayerView.setPlayer(mExoplayer);
            //prepare the media
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            if (videoPosition != 0) {
                mExoplayer.seekTo(videoPosition);
            }

            mExoplayer.prepare(mediaSource);

            mExoplayer.setPlayWhenReady(playWhenReady);
        }
    }

    private void releasePlayer() {
        if (mExoplayer != null) {
            videoAvailable = mExoplayer.getPlayWhenReady();
            mExoplayer.stop();
            mExoplayer.release();
            mExoplayer = null;
        }
    }

    //save current video position and state
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_RECIPE, recipe);
        outState.putParcelable(SAVED_STEP, mStep);
        if (mExoplayer != null) {
            videoPosition = mExoplayer.getCurrentPosition();
            outState.putLong(VIDEO_POSITION, videoPosition);
            playWhenReady = mExoplayer.getPlayWhenReady();
            outState.putBoolean(VIDEO_STATUS, playWhenReady);

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(Uri.parse(videoURL));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoplayer == null)) {
            initializePlayer(Uri.parse(videoURL));
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
}
