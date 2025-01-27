package com.video.vadoodemo;
import androidx.appcompat.app.AppCompatActivity;
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
import com.video.vadoolite.DataListener;
import com.video.vadoolite.VadooLoader;

public class MainActivity extends AppCompatActivity {


    private PlayerView playerView;
    private SimpleExoPlayer player;
    VadooLoader loader;
    int http_data;
    int p2p_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_main);
        TextView bandwidthmeter = findViewById(R.id.bandwidthmeter);
        loader = new VadooLoader(this);
        DataListener dataListener = new DataListener() {
            @Override
            public void onData(String method, int bytes) {
                if(method.equals("http")) {
                    http_data += bytes;
                } else {
                    p2p_data += bytes;
                }
                bandwidthmeter.setText("Http :- " + String.format("%.2f", http_data*1.0/(1024*1024)) + " P2p :- " + String.format("%.2f", p2p_data*1.0/(1024*1024)));
            }
        };
        loader.set_datalistener(dataListener);
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
        String VOD_URL = "https://d34mbcqhk5ge1k.cloudfront.net/index.m3u8";
        VOD_URL = loader.parseStreamUrl(VOD_URL);
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}