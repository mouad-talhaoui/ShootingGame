package com.mouad_ta.shootinggame;

import android.content.Context;
import android.graphics.Bitmap;

public class Explosion {
    private static Bitmap[] explosionBitmaps = new Bitmap[9];
    int explosionFrame;
    int eX, eY;

    public Explosion(Context context, int eX, int eY) {
        if (explosionBitmaps[0] == null) {
            explosionBitmaps[0] = Utils.getBitmapFromVectorDrawable(context, R.drawable.neon_explosion0);
            explosionBitmaps[1] = Utils.getBitmapFromVectorDrawable(context, R.drawable.neon_explosion1);
            explosionBitmaps[2] = Utils.getBitmapFromVectorDrawable(context, R.drawable.neon_explosion2);
            explosionBitmaps[3] = Utils.getBitmapFromVectorDrawable(context, R.drawable.neon_explosion3);
            explosionBitmaps[4] = Utils.getBitmapFromVectorDrawable(context, R.drawable.neon_explosion4);
            explosionBitmaps[5] = Utils.getBitmapFromVectorDrawable(context, R.drawable.neon_explosion5);
            explosionBitmaps[6] = Utils.getBitmapFromVectorDrawable(context, R.drawable.neon_explosion6);
            explosionBitmaps[7] = Utils.getBitmapFromVectorDrawable(context, R.drawable.neon_explosion7);
            explosionBitmaps[8] = Utils.getBitmapFromVectorDrawable(context, R.drawable.neon_explosion8);
        }
        explosionFrame = 0;
        this.eX = eX;
        this.eY = eY;
    }

    public Bitmap getExplosion(int explosionFrame){
        return explosionBitmaps[explosionFrame];
    }
}
