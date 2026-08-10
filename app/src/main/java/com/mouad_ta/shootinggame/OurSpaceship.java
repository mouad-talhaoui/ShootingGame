package com.mouad_ta.shootinggame;

import android.content.Context;
import android.graphics.Bitmap;
import java.util.Random;

public class OurSpaceship {
    Context context;
    Bitmap ourSpaceship;
    int ox, oy;
    Random random;

    public OurSpaceship(Context context) {
        this.context = context;
        ourSpaceship = Utils.getBitmapFromVectorDrawable(context, R.drawable.player_ship);
        random = new Random();
        // Ensure spaceship spawns within screen boundaries
        int maxWidth = Math.max(1, SpaceShooter.screenWidth - ourSpaceship.getWidth());
        ox = random.nextInt(maxWidth);
        oy = SpaceShooter.screenHeight - ourSpaceship.getHeight();
    }

    public Bitmap getOurSpaceship(){
        return ourSpaceship;
    }

    int getOurSpaceshipWidth(){
        return ourSpaceship.getWidth();
    }

    int getOurSpaceshipHeight(){
        return ourSpaceship.getHeight();
    }
}
