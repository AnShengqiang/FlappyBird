package com.example.charger.flappybird.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.charger.flappybird.utils.Util;

/**
 * Created by charger on 2017/8/15.
 */

public class Bird {

    private static final float RADIO_POS_HEIGHT = 2 / 3F;

    private static final int BIRD_SIZE = 30;

    private int x;

    private int y;

    private int mWidth;

    private int mHeight;

    private Bitmap mBitmap;

    private RectF mRectF = new RectF();

    public Bird(Context context, int gameWidth, int gameHeight, Bitmap bitmap) {
        this.mBitmap = bitmap;
        x = gameWidth/2 - bitmap.getWidth()/2;
        y = (int) (gameHeight * RADIO_POS_HEIGHT);
        mWidth = Util.dp2px(context, BIRD_SIZE);
        mHeight = (int) (mWidth * 1.0f / bitmap.getWidth() * bitmap.getHeight());
    }

    public void draw(Canvas canvas) {
        mRectF.set(x, y, x + mWidth, y + mHeight);
        canvas.drawBitmap(mBitmap, null, mRectF, null);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }
}
