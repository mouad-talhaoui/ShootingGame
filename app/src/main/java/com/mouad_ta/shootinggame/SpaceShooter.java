package com.mouad_ta.shootinggame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Random;

public class SpaceShooter extends View {

    Context context;
    Bitmap background, lifeImage;
    Handler handler;
    long UPDATE_MILLIS = 30;
    static int screenWidth, screenHeight;
    int points = 0;
    int life = 3;
    Paint scorePaint;
    int TEXT_PAINT = 80;
    boolean paused = false;
    OurSpaceship ourSpaceship;
    EnemySpaceship enemySpaceship;
    Random random;
    ArrayList<Shot> enemyShots, ourShots;
    ArrayList<Explosion> explosions;
    boolean enemyShotAction = false;
    Rect rect;

    final Runnable runnable = new Runnable() {
      @Override
      public void run(){
          invalidate();
      }
    };

    public SpaceShooter(Context context) {
        super(context);
        this.context = context;
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        random = new Random();
        enemyShots = new ArrayList<>();
        ourShots = new ArrayList<>();
        explosions = new ArrayList<>();
        ourSpaceship = new OurSpaceship(context);
        enemySpaceship = new EnemySpaceship(context);
        handler = new Handler(Looper.getMainLooper());
        
        // Use the new neon background vector
        background = Utils.getBitmapFromVectorDrawable(context, R.drawable.neon_background);
        // Using the new vector life icon
        lifeImage = Utils.getBitmapFromVectorDrawable(context, R.drawable.life_icon);
        
        scorePaint = new Paint();
        scorePaint.setColor(Color.CYAN);
        scorePaint.setTextSize(TEXT_PAINT);
        scorePaint.setTextAlign(Paint.Align.LEFT);
        scorePaint.setFakeBoldText(true);
        rect = new Rect(0, 0, screenWidth, screenHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 1. Draw Environment
        canvas.drawBitmap(background, null, rect, null);
        canvas.drawText("SCORE: " + points, 30, TEXT_PAINT + 30, scorePaint);
        
        for(int i=life; i>=1; i--){
            canvas.drawBitmap(lifeImage, screenWidth - (lifeImage.getWidth() + 20) * i, 30, null);
        }

        // 2. Check Game State
        if(life <= 0){
            paused = true;
            handler.removeCallbacks(runnable);
            Intent intent = new Intent(context, GameOver.class);
            intent.putExtra("points", points);
            context.startActivity(intent);
            ((Activity) context).finish();
            return;
        }

        // 3. Enemy Logic
        enemySpaceship.ex += enemySpaceship.enemyVelocity;
        if(enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() >= screenWidth || enemySpaceship.ex <= 0){
            enemySpaceship.enemyVelocity *= -1;
        }

        if(!enemyShotAction){
            Shot enemyShot = new Shot(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 2, enemySpaceship.ey + enemySpaceship.getEnemySpaceshipHeight(), false);
            enemyShots.add(enemyShot);
            enemyShotAction = true;
        }
        
        canvas.drawBitmap(enemySpaceship.getEnemySpaceship(), enemySpaceship.ex, enemySpaceship.ey, null);
        
        // 4. Player Logic
        if(ourSpaceship.ox > screenWidth - ourSpaceship.getOurSpaceshipWidth()){
            ourSpaceship.ox = screenWidth - ourSpaceship.getOurSpaceshipWidth();
        } else if(ourSpaceship.ox < 0){
            ourSpaceship.ox = 0;
        }
        canvas.drawBitmap(ourSpaceship.getOurSpaceship(), ourSpaceship.ox, ourSpaceship.oy, null);
        
        // 5. Shot & Collision Logic
        for(int i = enemyShots.size() - 1; i >= 0; i--){
            Shot s = enemyShots.get(i);
            s.shy += 15;
            canvas.drawBitmap(s.getShot(), s.shx - s.getShotWidth() / 2, s.shy, null);
            if(s.shx >= ourSpaceship.ox && 
               s.shx <= ourSpaceship.ox + ourSpaceship.getOurSpaceshipWidth() &&
               s.shy >= ourSpaceship.oy && 
               s.shy <= ourSpaceship.oy + ourSpaceship.getOurSpaceshipHeight()){
                life--;
                enemyShots.remove(i);
                explosions.add(new Explosion(context, ourSpaceship.ox + ourSpaceship.getOurSpaceshipWidth()/2, ourSpaceship.oy + ourSpaceship.getOurSpaceshipHeight()/2));
            } else if(s.shy >= screenHeight){
                enemyShots.remove(i);
            }
        }
        
        if(enemyShots.isEmpty()){
            enemyShotAction = false;
        }

        for(int i = ourShots.size() - 1; i >= 0; i--){
            Shot s = ourShots.get(i);
            s.shy -= 20; // Increased speed for neon theme
            canvas.drawBitmap(s.getShot(), s.shx - s.getShotWidth() / 2, s.shy, null);
            if(s.shx >= enemySpaceship.ex && 
               s.shx <= enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() &&
               s.shy <= enemySpaceship.ey + enemySpaceship.getEnemySpaceshipHeight() && 
               s.shy >= enemySpaceship.ey){
                points++;
                int explosionX = enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 2;
                int explosionY = enemySpaceship.ey + enemySpaceship.getEnemySpaceshipHeight() / 2;
                ourShots.remove(i);
                enemySpaceship.resetEnemy(); 
                explosions.add(new Explosion(context, explosionX, explosionY));
            } else if(s.shy <= 0){
                ourShots.remove(i);
            }
        }
        
        // 6. Animation Logic
        for(int i = explosions.size() - 1; i >= 0; i--){
            Explosion ex = explosions.get(i);
            Bitmap b = ex.getExplosion(ex.explosionFrame);
            canvas.drawBitmap(b, ex.eX - b.getWidth()/2, ex.eY - b.getHeight()/2, null);
            ex.explosionFrame++;
            if(ex.explosionFrame > 8){
                explosions.remove(i);
            }
        }
        
        if(!paused)
            handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int)event.getX();
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(ourShots.size() < 2){ // Allowed 2 shots for better neon gameplay
                ourShots.add(new Shot(context, ourSpaceship.ox + ourSpaceship.getOurSpaceshipWidth() / 2, ourSpaceship.oy, true));
            }
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
            ourSpaceship.ox = touchX - ourSpaceship.getOurSpaceshipWidth() / 2;
        }
        return true;
    }

    public void pause() {
        paused = true;
        handler.removeCallbacks(runnable);
    }

    public void resume() {
        paused = false;
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }
}
