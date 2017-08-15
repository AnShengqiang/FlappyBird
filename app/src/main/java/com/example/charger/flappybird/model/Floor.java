package com.example.charger.flappybird.model;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;

/**
 * Created by anshengqiang on 2017/8/15.
 */

public class Floor {

    private static final float FLOOR_Y_POS_RADIO = 4 / 5F;

    private int x;

    private int y;

    private BitmapShader mFloorShader;

    private int mGameWidth;

    private int mGameHeight;

    public Floor(int gameWidth, int gameHeight, Bitmap floorBg) {
        mGameWidth = gameWidth;
        mGameHeight = gameHeight;
        y = (int) (gameHeight * FLOOR_Y_POS_RADIO);
        mFloorShader = new BitmapShader(floorBg, TileMode.REPEAT, TileMode.CLAMP);
    }

    public void draw(Canvas canvas, Paint paint) {
        if (-x > mGameWidth) {
            x = x % mGameWidth;
        }
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(x, y);
        paint.setShader(mFloorShader);
        canvas.drawRect(x, 0, -x + mGameWidth, mGameHeight - y, paint);
        canvas.restore();
        paint.setShader(null);
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
}
