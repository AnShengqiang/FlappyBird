package com.example.charger.flappybird.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.charger.flappybird.view.FlappyBirdSurfaceView;

/**
 * Created by anshengqiang on 2017/8/15.
 */

public class FlappyBirdActivity extends Activity {

    private static final String TAG = "FlappyBirdActivity";

    FlappyBirdSurfaceView mFlappyBird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mFlappyBird = new FlappyBirdSurfaceView(this);
        setContentView(mFlappyBird);
        Log.i(TAG, "onCreate!");
    }
}
