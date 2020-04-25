package com.video.exoplayer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_list);
        recyclerView = findViewById(R.id.channels);

        String[] channels = {"Indiatv", "Zoom", "DD National", "Movieplus", "Bloomberg", "Gaming news", "Shortfilms"};
        String[] urls = {"https://m-c02-j2apps.s.llnwi.net/hls/0091.IndiaTV.in_144p/index.m3u8", "https://m-c07-j2apps.s.llnwi.net/hls/0069.Zoom.in.m3u8", "https://m-c036-j2apps.s.llnwi.net/hls/0098.DDNational.in_360p/index.m3u8", "https://m-c03-j2apps.s.llnwi.net/hls/6521.Movieplus.in_288p/index.m3u8", "https://m-c29-j2apps.s.llnwi.net/hls/5656.BloombergQuint.in_144p/index.m3u8", "https://m-c09-j2apps.s.llnwi.net/hls/8006.GamingTVEG.in_240p/index.m3u8", "https://m-c09-j2apps.s.llnwi.net/hls/8011.ShortFilmsTV.in_360p/index.m3u8"};
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Channel_listener listener = item -> {
            Intent intent = new Intent(MainActivity.this, Player_Activity.class);
            intent.putExtra("url", urls[item]);
            startActivity(intent);
        };
        mAdapter = new Channel_Adapter(channels, listener);
        recyclerView.setAdapter(mAdapter);
    }
}