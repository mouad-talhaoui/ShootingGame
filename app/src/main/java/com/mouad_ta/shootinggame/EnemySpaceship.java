package com.mouad_ta.shootinggame;

import android.content.Context;
import android.graphics.Bitmap;
import java.util.Random;

public class EnemySpaceship {
    Context context;
    Bitmap enemySpaceship;
    int ex, ey;
    int enemyVelocity;
    Random random;

    public EnemySpaceship(Context context) {
        this.context = context;
        enemySpaceship = Utils.getBitmapFromVectorDrawable(context, R.drawable.enemy_ship);
        random = new Random();
        resetEnemy();
    }

    public void resetEnemy() {
        // Ensure enemy spawns within screen width
        int maxWidth = SpaceShooter.screenWidth - getEnemySpaceshipWidth();
        ex = random.nextInt(Math.max(1, maxWidth));
        ey = 0;
        enemyVelocity = 14 + random.nextInt(10);
    }

    public Bitmap getEnemySpaceship(){
        return enemySpaceship;
    }

    int getEnemySpaceshipWidth(){
        return enemySpaceship.getWidth();
    }

    int getEnemySpaceshipHeight(){
        return enemySpaceship.getHeight();
    }
}
