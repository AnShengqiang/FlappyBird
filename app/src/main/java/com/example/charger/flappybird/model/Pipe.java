package com.example.charger.flappybird.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by anshengqiang on 2017/8/16.
 */

public class Pipe {

    private static final float RATIO_BETWEEN_UP_DOWN = 1 / 5F;

    private static final float RATIO_PIPE_MAX_HEIGHT = 5 / 10F;

    private static final float RATIO_PIPE_MIN_HEIGHT = 1 / 10F;

    private static Random random = new Random();

    private int mPipeX;
    private int mPipeHeight;
    private int mSpaceBetweenTwoPipes;
    private Bitmap mTopPipeBitmap;
    private Bitmap mBottomPipeBitmap;

    public Pipe(int pipeX, int gameHeight, Bitmap topPipeBitmap, Bitmap bottomPipeBitmap) {
        mPipeX = pipeX;
        randomHeight(gameHeight);
        mSpaceBetweenTwoPipes = (int) (gameHeight * RATIO_BETWEEN_UP_DOWN);
        mTopPipeBitmap = topPipeBitmap;
        mBottomPipeBitmap = bottomPipeBitmap;
    }

    private void randomHeight(int gameHeight) {
        mPipeHeight = random
                .nextInt((int) (gameHeight * (RATIO_PIPE_MAX_HEIGHT - RATIO_PIPE_MIN_HEIGHT)));
        mPipeHeight = (int) (mPipeHeight + gameHeight * RATIO_PIPE_MIN_HEIGHT);
    }

    public void draw(Canvas canvas, RectF rect) {
        canvas.save();
        canvas.translate(mPipeX, -(rect.bottom - mPipeHeight));
        canvas.drawBitmap(mTopPipeBitmap, null, rect, null);
        canvas.translate(0, rect.bottom + mSpaceBetweenTwoPipes);
        canvas.drawBitmap(mBottomPipeBitmap, null, rect, null);
        canvas.restore();
    }

    public boolean touchBird(Bird mBird) {
        if (mBird.getX() + mBird.getBirdWidth()> mPipeX
                && (mBird.getY() < mPipeHeight || mBird.getY() + mBird.getBirdHeight() > mPipeHeight
                + mSpaceBetweenTwoPipes)) {
            return true;
        }
        return false;
    }

    public int getPipeX() {
        return mPipeX;
    }

    public void setPipeX(int pipeX) {
        this.mPipeX = pipeX;
    }

}
