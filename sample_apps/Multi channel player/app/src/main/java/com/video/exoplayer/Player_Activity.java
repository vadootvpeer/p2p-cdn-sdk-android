package com.video.exoplayer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import fr.pchab.webrtcclient.VadooEngine;

public class Player_Activity extends Activity {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    long position = -1;
    TextView bandwidthmeter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.main);
        bandwidthmeter = findViewById(R.id.bandwith_meter);

        init_video();
    }

    class VideoPlayerConfig {
        //Minimum Video you want to buffer while Playing
        public static final int MIN_BUFFER_DURATION = 7000;
        //Max Video you want to buffer during PlayBack
        public static final int MAX_BUFFER_DURATION = 15000;
        //Min Video you want to buffer before start Playing it
        public static final int MIN_PLAYBACK_START_BUFFER = 7000;
        //Min video You want to buffer when user resumes video
        public static final int MIN_PLAYBACK_RESUME_BUFFER = 7000;
    }

    private void init_video() {
        DataSource.Factory dataSourceFactory =
                new DefaultHttpDataSourceFactory(
                        Util.getUserAgent(this, "p2p-engine"),
                        DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                        DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                        true   // allowCrossProtocolRedirects
                );
        LoadControl loadControl = new DefaultLoadControl.Builder()
                .setAllocator(new DefaultAllocator(true, 16))
                .setBufferDurationsMs(VideoPlayerConfig.MIN_BUFFER_DURATION,
                        VideoPlayerConfig.MAX_BUFFER_DURATION,
                        VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                        VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER)
                .setTargetBufferBytes(-1)
                .setPrioritizeTimeOverSizeThresholds(true).createDefaultLoadControl();
        String VOD_URL = getIntent().getStringExtra("url");
        VadooEngine engine = ((Vadoo_Application)getApplication()).getEngine();
        VOD_URL = engine.parseStreamUrl(this, VOD_URL);
        HlsMediaSource hlsMediaSource =
                new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(VOD_URL));
        playerView = findViewById(R.id.player_view);
        TrackSelector trackSelector = new DefaultTrackSelector();
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        playerView.setPlayer(player);
        player.prepare(hlsMediaSource);
        player.setPlayWhenReady(true);
    }

    @Override
    public void onBackPressed(){
        try{
            if (player != null) {
                player.setPlayWhenReady(false);
                player.stop();
                player.seekTo(0);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(player != null && player.getPlayWhenReady()) {
            position = player.getCurrentPosition();
            player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(player != null) {
            if(position!=-1) {
                player.seekTo(position);
            }
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }
}