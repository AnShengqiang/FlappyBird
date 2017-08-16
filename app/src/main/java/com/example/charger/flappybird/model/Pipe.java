package com.example.charger.flappybird.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by anshengqiang on 2017/8/16.
 */

public class Pipe {

    private static final float RATIO_BETWEEN_UP_DOWN = 1/5F;

    private static final float RATIO_PIPE_MAX_HEIGHT = 2/5F;

    private static final float RATIO_PIPE_MIN_HEIGHT = 1/5F;

    private static Random random = new Random();

    private int x;
    private int height;
    private int margin;
    private Bitmap mTop;
    private Bitmap mBottom;

    public Pipe(Context context, int gameWidth, int gameHeight, Bitmap top,
                Bitmap bottom) {
        margin = (int) (gameHeight * RATIO_BETWEEN_UP_DOWN);

        x = gameWidth;

        mTop = top;
        mBottom = bottom;

        randomHeight(gameHeight);

    }

    private void randomHeight(int gameHeight) {
        height = random
                .nextInt((int) (gameHeight * (RATIO_PIPE_MAX_HEIGHT - RATIO_PIPE_MIN_HEIGHT)));
        height = (int) (height + gameHeight * RATIO_PIPE_MIN_HEIGHT);
    }

    public void draw(Canvas canvas, RectF rect) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(x, -(rect.bottom - height));
        canvas.drawBitmap(mTop, null, rect, null);
        canvas.translate(0, (rect.bottom - height) + height + margin);
        canvas.drawBitmap(mBottom, null, rect, null);
        canvas.restore();
    }

    public boolean touchBird(Bird mBird) {
        if (mBird.getX() + mBird.getBirdWidth() > x
                && (mBird.getY() < height || mBird.getY() + mBird.getBirdHeight() > height
                + margin)) {
            return true;
        }
        return false;

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

}