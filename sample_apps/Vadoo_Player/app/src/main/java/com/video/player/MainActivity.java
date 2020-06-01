package com.video.player;

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
import com.vadoo.sdk.BandwidthListener;
import com.vadoo.sdk.VadooEngine;

public class MainActivity extends AppCompatActivity {

    private PlayerView playerView;
    private SimpleExoPlayer player;
    TextView bandwidthmeter;
    int http_data = 0;
    int p2p_data = 0;

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
        String[] urls = {"https://m-c02-j2apps.s.llnwi.net/hls/0091.IndiaTV.in_144p/index.m3u8", "https://m-c07-j2apps.s.llnwi.net/hls/0069.Zoom.in.m3u8", "https://m-c036-j2apps.s.llnwi.net/hls/0098.DDNational.in_360p/index.m3u8", "https://m-c03-j2apps.s.llnwi.net/hls/6521.Movieplus.in_288p/index.m3u8", "https://m-c29-j2apps.s.llnwi.net/hls/5656.BloombergQuint.in_144p/index.m3u8", "https://m-c09-j2apps.s.llnwi.net/hls/8006.GamingTVEG.in_240p/index.m3u8", "https://m-c09-j2apps.s.llnwi.net/hls/8011.ShortFilmsTV.in_360p/index.m3u8"};
        String VOD_URL = urls[2];
        BandwidthListener bandwidthListener = (size, p2p) -> {
            if (p2p) {
                p2p_data += size;
            } else {
                http_data += size;
            }
            MainActivity.this.runOnUiThread(() -> bandwidthmeter.setText("Http :- " + http_data / 1000000.0 + "MB\n" + "P2p :- " + p2p_data / 1000000.0 + "MB\nSaved " + ((p2p_data * 100) / (p2p_data + http_data)) + "%"));
        };
        VadooEngine engine = new VadooEngine(this.getApplicationContext());
        engine.setBandwidthlistener(bandwidthListener);
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