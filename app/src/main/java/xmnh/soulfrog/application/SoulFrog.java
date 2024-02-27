package xmnh.soulfrog.application;

import android.app.Application;

public class SoulFrog extends Application {
    public static final String TAG = "SoulFrog";
    public static SoulFrog instance;

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }
    }

}
