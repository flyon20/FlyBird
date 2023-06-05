package com.example.flybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;


public class Pipe {
    private Bitmap pipe; // 管道的位图
    private int x,y; // 管道的坐标
    private int screenW,screenH; // 屏幕的宽度和高度
    public  int type; // 管道的类型：普通或特殊
    public boolean isOver=false; // 是否越过了该管道
    public boolean isTake=false; // 小鸟是否穿过了该管道

    public Pipe(Bitmap pipe, int x, int y, int screenW, int screenH, int type) {
        this.pipe = pipe;
        this.x = x;
        this.y = y;
        this.screenW = screenW;//窗口宽度
        this.screenH = screenH;//窗口高度
        this.type = type;
    }

    public void draw(Canvas canvas, Paint paint){//绘制管道
        canvas.drawBitmap(pipe,x,y,paint);
    }//绘画画布
    public void logic(){
        x -= 10; // 将x坐标向左移动10个像素
        if (x + pipe.getWidth() <= screenW / 5 && type == GameProperty.PIPE_NORMAL && isTake == false) {
            // 当该管道是普通管道且小鸟穿过该管道，并且此时管道即将完全离开屏幕时，则视为越过该管道
            isOver = true;
        }
        if (x <= -200) {
            if (type == GameProperty.PIPE_SPECIAL) {
                // 如果是特殊管道，则将其移动到屏幕的最右侧以便实现循环利用
                x = screenW / 3 * 3;
            }
            if (type == GameProperty.PIPE_NORMAL) {
                // 如果是普通管道，则将其移到屏幕的最右侧以便实现循环利用，并设置相应的标志位
                x = screenW / 3 * 3;
                isTake = false;
                isOver = false;
            }
        }
    }
    public boolean Collision(Bird bird){
        if (bird.x + bird.bitmaps[bird.i].getWidth() < x) {
            // 如果小鸟在管道的左侧，则认为不发生碰撞
            return false;
        } else if (bird.y > y + pipe.getHeight()) {
            // 如果小鸟在管道的上侧，则认为不发生碰撞
            return false;
        } else if (bird.y + bird.bitmaps[bird.i].getHeight() < y) {
            // 如果小鸟在管道的下侧，则认为不发生碰撞
            return false;
        } else if (bird.x > pipe.getWidth() + x) {
            // 如果小鸟在管道的右侧，则认为不发生碰撞
            return false;
        }

        return true; // 如果以上四个条件都不满足，则认为发生了碰撞
    }
}
