package com.example.flybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bird {
    public Bitmap[] bitmaps;
    private int ScreenW,screenH;
    public int x,y;
    public int i=2;
    private boolean isUP=false;

    //小鸟重力加速度
    private double Gy=5;
    public double bird_vy=0;
    public double bird_up_y=-35;


    public Bird(Bitmap[] bitmaps, int screenW, int screenH) {
        this.bitmaps = bitmaps;
        this.ScreenW = screenW;
        this.screenH = screenH;
        x=screenW/5;
        y=screenH/2-200;
    }
    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bitmaps[i],x,y,paint);
    }
    //逻辑函数
    public void logic(){
        if (i==2){
            isUP=false;
        } else if (i==0) {
            isUP=true;
        }
        if (isUP){
            i++;
        } else {
            i--;
        }
        //下落速度
        if (y<=screenH-100){
            bird_vy+=Gy;
            y+=bird_vy;
        }
    }
    public void ToUp(){     //上升速度
        for (int j=0;j<=50;j++){
            bird_vy=bird_up_y;
        }
    }


}
