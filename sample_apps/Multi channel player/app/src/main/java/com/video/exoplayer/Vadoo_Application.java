package com.video.exoplayer;

import android.app.Application;
import fr.pchab.webrtcclient.VadooEngine;

public class Vadoo_Application extends Application {
    private VadooEngine engine = null;
    @Override
    public void onCreate() {
        super.onCreate();
        if(engine == null) {
            engine = new VadooEngine(this);
        }
    }

    public VadooEngine getEngine() {
        return engine;
    }
}