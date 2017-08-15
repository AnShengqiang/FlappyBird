package com.example.charger.flappybird.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.example.charger.flappybird.R;
import com.example.charger.flappybird.model.Bird;
import com.example.charger.flappybird.model.Floor;
import com.example.charger.flappybird.model.Pipe;
import com.example.charger.flappybird.utils.Util;

import java.util.ArrayList;
import java.util.List;


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

    private Paint mPaint;
    private Floor mFloor;
    private Bitmap mFloorBitmap;
    private int mSpeed;

    private Bitmap mPipeTop;
    private Bitmap mPipeBottom;
    private RectF mPipeRect;
    private int mPipeWidth;

    private final int[] mNums = new int[]{R.drawable.n0, R.drawable.n1,
            R.drawable.n2, R.drawable.n3, R.drawable.n4, R.drawable.n5,
            R.drawable.n6, R.drawable.n7, R.drawable.n8, R.drawable.n9};
    private Bitmap[] mNumBitmap;
    private int mGrade = 100;

    private static final float RADIO_SINGLE_NUM_HEIGHT = 1 / 15f;

    private int mSingleGradeWidth;

    private int mSingleGradeHeight;

    private RectF mSingleNumRectF;

    private static final int PIPE_WIDTH = 60;

    private List<Pipe> mPipes = new ArrayList<Pipe>();

    private enum GameStatus {
        WAITTING, RUNNING, STOP;
    }

    private GameStatus mStatus = GameStatus.WAITTING;

    private static final int TOUCH_UP_SIZE = -16;

    private final int mBirdUpDis = Util.dp2px(getContext(), TOUCH_UP_SIZE);

    private int mTmpBirdDis;

    private final int mAutoDownSpeed = Util.dp2px(getContext(), 2);

    private final int PIPE_DIS_BETWEEN_TWO = Util.dp2px(getContext(), 100);

    private int mTmpMoveDistance;

    private List<Pipe> mNeedRemovePipe = new ArrayList<Pipe>();

    private int mRemovedPipe = 0;

    public FlappyBirdSurfaceView(Context context) {
        this(context, null);
    }

    public FlappyBirdSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);

        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

        initBitmaps();

        mSpeed = Util.dp2px(getContext(), 2);
        mPipeWidth = Util.dp2px(getContext(), PIPE_WIDTH);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isRunning = true;
        mThread = new Thread(this);
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
            logic();
            draw();
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
        mBird = new Bird(getContext(), mWidth, mHeight, mBirdBitmap);
        mFloor = new Floor(mWidth, mHeight, mFloorBitmap);

        mPipeRect = new RectF(0, 0, mPipeWidth, mHeight);

        mSingleGradeHeight = (int) (h * RADIO_SINGLE_NUM_HEIGHT);
        mSingleGradeWidth = (int) (mSingleGradeHeight * 1.0f
                / mNumBitmap[0].getHeight() * mNumBitmap[0].getWidth());
        mSingleNumRectF = new RectF(0, 0, mSingleGradeWidth, mSingleGradeHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            switch (mStatus) {
                case WAITTING:
                    mStatus = GameStatus.RUNNING;
                    break;
                case RUNNING:
                    mTmpBirdDis = mBirdUpDis;
                    break;
            }
        }
        return true;
    }

    private void logic() {
        switch (mStatus) {
            case RUNNING:
                mGrade = 0;
                mFloor.setX(mFloor.getX() - mSpeed);
                logicPipe();
                mTmpBirdDis += mAutoDownSpeed;
                mBird.setY(mBird.getY() + mTmpBirdDis);
                mGrade += mRemovedPipe;
                for (Pipe pipe : mPipes) {
                    if (pipe.getX() + mPipeWidth < mBird.getX()) {
                        mGrade++;
                    }
                }
                checkGameOver();
                break;
            case STOP:
                if (mBird.getY() < mFloor.getY() - mBird.getWidth()) {
                    mTmpBirdDis += mAutoDownSpeed;
                    mBird.setY(mBird.getY() + mTmpBirdDis);
                } else {
                    mStatus = GameStatus.WAITTING;
                    initPos();
                }
                break;
            default:
                break;
        }

    }

    private void draw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            if (mCanvas != null) {
                drawBackground();
                drawBird();
                drawPipes();
                drawFloor();
                drawGrades();
                mFloor.setX(mFloor.getX() - mSpeed);
            }
        } catch (Exception e) {
            Log.i("FlappyBirdSV", "出现不明错误");
        } finally {
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void initBitmaps() {
        mBackground = loadImageByResId(R.drawable.bg1);
        mBirdBitmap = loadImageByResId(R.drawable.b1);
        mFloorBitmap = loadImageByResId(R.drawable.floor_bg2);
        mPipeTop = loadImageByResId(R.drawable.g2);
        mPipeBottom = loadImageByResId(R.drawable.g1);
        mNumBitmap = new Bitmap[mNums.length];
        for (int i = 0; i < mNumBitmap.length; i++) {
            mNumBitmap[i] = loadImageByResId(mNums[i]);
        }
    }

    private void drawBackground() {
        mCanvas.drawBitmap(mBackground, null, mGamePanelRect, null);
    }

    private void drawBird() {
        mBird.draw(mCanvas);
    }

    private void drawFloor() {
        mFloor.draw(mCanvas, mPaint);
    }

    private void drawPipes() {
        for (Pipe pipe : mPipes) {
            pipe.setX(pipe.getX() - mSpeed);
            pipe.draw(mCanvas, mPipeRect);
        }
    }

    private void drawGrades() {
        String grade = mGrade + "";
        mCanvas.save(Canvas.MATRIX_SAVE_FLAG);
        mCanvas.translate(mWidth / 2 - grade.length() * mSingleGradeWidth / 2,
                1f / 8 * mHeight);
        // draw single num one by one
        for (int i = 0; i < grade.length(); i++) {
            String numStr = grade.substring(i, i + 1);
            int num = Integer.valueOf(numStr);
            mCanvas.drawBitmap(mNumBitmap[num], null, mSingleNumRectF, null);
            mCanvas.translate(mSingleGradeWidth, 0);
        }
        mCanvas.restore();
    }

    private void logicPipe() {
        for (Pipe pipe : mPipes) {
            if (pipe.getX() < -mPipeWidth) {
                mNeedRemovePipe.add(pipe);
                mRemovedPipe++;
                continue;
            }
            pipe.setX(pipe.getX() - mSpeed);
        }
        mPipes.removeAll(mNeedRemovePipe);
        mNeedRemovePipe.clear();

        mTmpMoveDistance += mSpeed;
        if (mTmpMoveDistance >= PIPE_DIS_BETWEEN_TWO) {
            Pipe pipe = new Pipe(getContext(), getWidth(), getHeight(),
                    mPipeTop, mPipeBottom);
            mPipes.add(pipe);
            mTmpMoveDistance = 0;
        }
    }

    private Bitmap loadImageByResId(int id) {
        return BitmapFactory.decodeResource(getResources(), id);
    }

    private void checkGameOver() {

        if (mBird.getY() > mFloor.getY() - mBird.getHeight()) {
            mStatus = GameStatus.STOP;
        }
        for (Pipe wall : mPipes) {
            if (wall.getX() + mPipeWidth < mBird.getX()) {
                continue;
            }
            if (wall.touchBird(mBird)) {
                mStatus = GameStatus.STOP;
                break;
            }
        }
    }

    private void initPos() {
        mPipes.clear();
        mNeedRemovePipe.clear();
        mBird.setY(mHeight * 2 / 3);
        mTmpBirdDis = 0;
        mTmpMoveDistance = 0;
    }
}
