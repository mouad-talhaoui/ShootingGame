package com.mouad_ta.shootinggame;

import android.content.Context;
import android.graphics.Bitmap;

public class Shot {
    private static Bitmap playerShotBitmap;
    private static Bitmap enemyShotBitmap;
    private Bitmap currentShot;
    int shx, shy;

    public Shot(Context context, int shx, int shy) {
        this(context, shx, shy, true);
    }

    public Shot(Context context, int shx, int shy, boolean isPlayerShot) {
        if (playerShotBitmap == null) {
            playerShotBitmap = Utils.getBitmapFromVectorDrawable(context, R.drawable.player_shot);
        }
        if (enemyShotBitmap == null) {
            enemyShotBitmap = Utils.getBitmapFromVectorDrawable(context, R.drawable.enemy_shot);
        }
        
        this.currentShot = isPlayerShot ? playerShotBitmap : enemyShotBitmap;
        this.shx = shx;
        this.shy = shy;
    }

    public Bitmap getShot() {
        return currentShot;
    }

    public int getShotWidth() {
        return currentShot.getWidth();
    }

    public int getShotHeight() {
        return currentShot.getHeight();
    }
}
