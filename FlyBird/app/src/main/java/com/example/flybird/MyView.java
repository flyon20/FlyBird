package com.example.flybird;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import java.util.Random;
import java.util.Vector;

public class MyView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private SurfaceHolder holder;
    private Resources resources;
    private Bitmap bg;
    private Thread th;
    private boolean flag;
    private Rect bgRect;
    private Canvas canvas;//画布
    private Paint paint;//画笔
    private Bird bird;
    //管道参数
    private Vector<Pipe> pipes;

    //游戏状态
    public static int GameState=GameProperty.GAME_START;
    public static int score=0;
    public boolean runfalg=false;
    public MyView(Context context) {
        super(context);
         holder = getHolder();
         resources = getResources();
         this.holder.addCallback(this);
         setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        //NonNull表示目标对象不能为空
        initGame();

    }

    public void initGame(){
        // 加载背景图
        bg = BitmapFactory.decodeResource(resources, GameProperty.GAME_BG);
        // 创建当前对象的线程
        th = new Thread(this);
        // 设置背景矩形区域为整个屏幕
        bgRect = new Rect(0, 0, getWidth(), getHeight());
        // 创建画笔对象并设置颜色和文本大小
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(100);
        // 初始化小鸟
        initBird();
        // 初始化水管
        initPipe();
        // 启动当前对象的线程
        flag = true;
        th.start();
        score=0;
    }

    private void initPipe() {
        // 创建随机数生成器
        Random random = new Random();
        // 创建水管向量
        pipes = new Vector<>();
        // 创建 3 对水管
        for (int i = 0; i < 3; i++) {
            // 生成随机数，用于控制水管高度
            int randomNumber = random.nextInt(30) + 40;
            // 加载上下两部分水管图片，并创建对应的 Pipe 对象
            Bitmap pipe01 = BitmapFactory.decodeResource(resources, GameProperty.pipe_down);
            Bitmap pipe02 = BitmapFactory.decodeResource(resources, GameProperty.pipe_up);
            Pipe pipe_down = new Pipe(pipe01, getWidth() / 2 + 200 + (getWidth() - 200) * i, 10 * randomNumber - 100,
                    getWidth(), getHeight(), GameProperty.PIPE_NORMAL);// 上半部分水管
            Pipe pipe_up = new Pipe(pipe02, getWidth() / 2 + 200 + (getWidth() - 200) * i, 10 * randomNumber + 600,
                    getWidth(), getHeight(), GameProperty.PIPE_NORMAL);// 下半部分水管
            // 将上下两部分水管加入到 pipes 向量中
            pipes.add(pipe_up);
            pipes.add(pipe_down);
            // 创建管道柱子
            for (int j = 1; j < 10; j++) {
                // 加载水管柱子图片，并创建对应的 Pipe 对象
                Bitmap pipe_mid = BitmapFactory.decodeResource(resources, GameProperty.pipe_mid);
                Pipe pipeup = new Pipe(pipe_mid, getWidth() / 2 + 210 + (getWidth() - 200) * i,  -50- (pipe_mid.getWidth()-15)
                        * j + 10 * randomNumber, getWidth(), getHeight(), GameProperty.PIPE_SPECIAL);// 上半部分柱子
                Pipe pipedown = new Pipe(pipe_mid, getWidth() / 2 + 210 + (getWidth() - 200) * i, 520 + (pipe_mid.getWidth()-15)
                        * j + 10 * randomNumber, getWidth(), getHeight(), GameProperty.PIPE_SPECIAL);// 下半部分柱子
                // 将上下两部分柱子加入到 pipes 向量中
                pipes.add(pipeup);
                pipes.add(pipedown);
            }
        }
    }


    private void initBird() {
        // 加载小鸟的三个动画帧
        Bitmap bird1 = BitmapFactory.decodeResource(resources, GameProperty.bird1);
        Bitmap bird2 = BitmapFactory.decodeResource(resources, GameProperty.bird2);
        Bitmap bird3 = BitmapFactory.decodeResource(resources, GameProperty.bird3);
        Bitmap[] bitmaps = new Bitmap[]{bird1, bird2, bird3};
        // 创建小鸟对象，并设置屏幕的宽度和高度
        bird = new Bird(bitmaps, getWidth(), getHeight());
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void run() {
        while (flag) {
            // 调用 myDraw() 函数绘制游戏画面
            myDraw();
            // 调用 logic() 函数处理游戏逻辑
            logic();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void logic() {
        if(runfalg){
            // 处理小鸟的逻辑
            bird.logic();
            // 处理所有水管的逻辑
            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.elementAt(i);
                pipe.logic();
            }
        }


        // 处理碰撞检测的逻辑
        logicCollision();
        // 判断游戏是否结束
        isOver();
    }

    // 绘制游戏画面的函数
    private void myDraw() {
        try {
            // 获取 SurfaceHolder 对象
            canvas = holder.lockCanvas();
            // 在画布上绘制背景图
            canvas.drawBitmap(bg, null, bgRect, paint);

            //绘制开始游戏
            if (!runfalg) {
                canvas.drawText("点击开始游戏",getWidth() / 2 - 200, getHeight() / 2 + 100, paint);
            }
            // 根据当前状态不同，绘制不同的游戏元素
            switch (GameState) {
                case GameProperty.GAME_START:
                    // 绘制小鸟
                    bird.draw(canvas, paint);
                    // 绘制水管
                    for (int i = 0; i < pipes.size(); i++) {
                        Pipe pipe = pipes.elementAt(i);
                        pipe.draw(canvas, paint);
                    }
                    // 绘制得分
                    canvas.drawText("分数：" + score / 2, 60, 100, paint);
                    break;
                case GameProperty.GAME_LOSE:
                    // 绘制游戏结束信息
                    canvas.drawText("游戏失败", getWidth() / 2 - 200, getHeight() / 2 + 100, paint);
                    canvas.drawText("点击重来", getWidth() / 2 - 200, getHeight() / 2 + 220, paint);
                    // 绘制指定图片
                    Drawable icon = getResources().getDrawable(R.drawable.my_icon);
                    icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    int centerX = canvas.getWidth() / 2;
                    int centerY = canvas.getHeight() / 2 - 300;
                    int width = icon.getIntrinsicWidth();
                    int height = icon.getIntrinsicHeight();
                    icon.setBounds(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2);
                    icon.draw(canvas);
                    // 结束游戏线程
                    flag = true;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 解锁画布，并提交绘制内容
            holder.unlockCanvasAndPost(canvas);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(!runfalg){
                runfalg=true;
            }
            // 小鸟上升
            bird.ToUp();
        }
        if (GameState==GameProperty.GAME_LOSE){
            GameState=GameProperty.GAME_START;
            initGame();
            runfalg=false;
        }

        return true;
    }

    // 碰撞检测的逻辑函数
    private void logicCollision() {
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.elementAt(i);
            // 判断是否碰撞
            if (pipe.Collision(bird)) {
                // 碰撞后游戏结束
                GameState = GameProperty.GAME_LOSE;
            }
        }
    }

    private void isOver() {
        for (int i = 0; i < pipes.size() - 10; i++) {
            Pipe pipe = pipes.elementAt(i);
            // 判断是否通过水管，并增加得分
            if (pipe.isOver && pipe.type == GameProperty.PIPE_NORMAL) {
                score++;
                pipe.isOver = false;
                pipe.isTake = true;
                break;
            }
        }
    }
}
