package com.example.charger.flappybird.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.example.charger.flappybird.R;
import com.example.charger.flappybird.model.Bird;


/**
 * Created by charger on 2017/8/15.
 */

public class FlappyBirdSurfaceView extends SurfaceView implements Callback, Runnable {

    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Thread mThread;
    private boolean isRunning;

    private int mWidth;
    private int mHeight;
    private RectF mGamePanelRect = new RectF();
    private Bitmap mBackground;

    private Bird mBird;
    private Bitmap mBirdBitmap;


    public FlappyBirdSurfaceView(Context context) {
        this(context, null);
    }

    public FlappyBirdSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);

        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

        initBitmaps();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.isRunning = true;
        this.mThread = new Thread();
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            long start = System.currentTimeMillis();
            draw(mCanvas);
            long end = System.currentTimeMillis();
            try {
                if ((end - start) < 50) {
                    mThread.sleep(50 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
        mGamePanelRect.set(0, 0, w, h);
    }

    private void draw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            if (mCanvas != null) {
                drawBackground();
            }
        } catch (Exception e) {
            Log.i("FlappyBirdSV","出现不明错误");
        } finally {
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void initBitmaps() {
        mBackground = loadImageByResId(R.drawable.bg1);
        mBirdBitmap = loadImageByResId(R.drawable.b1);
    }

    private void drawBackground() {
        mCanvas.drawBitmap(mBackground, null, mGamePanelRect, null);
    }

    private Bitmap loadImageByResId(int id) {
        return BitmapFactory.decodeResource(getResources(), id);
    }
}
