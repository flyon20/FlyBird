package com.example.flybird;
//变量定义
public class GameProperty {
    public static final int GAME_BG=R.mipmap.bg;//读入背景图
    public static final int bird1=R.mipmap.bird1;//读入小鸟角色图1-3,实现小鸟飞行动画
    public static final int bird2=R.mipmap.bird2;
    public static final int bird3=R.mipmap.bird3;

    //管道
    public static final int pipe_up=R.mipmap.pipe_down;//下管道图
    public static final int pipe_down=R.mipmap.pipe_up;//上管道图

    public static final int pipe_mid=R.mipmap.pipe_mid;//管道身体，用于拼接管道
    public static final int PIPE_NORMAL=1;
    public static final int PIPE_SPECIAL=2;
    //START,LOSE用于Switch的case 参考值
    public static final int GAME_START=1;
    public static final int GAME_LOSE=2;
}
