package xdy.gradientview;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.Random;

/**
 * 仿Instagram 登陆页面的颜色渐变效果
 * 本例子效果是随机获取2种颜色做渐变
 * 您可以自己设置2种相近的颜色做渐变,这样效果会比较好看一点
 * Created by 王小强 on 2017/2/23 0023.
 */
public class GradientView extends View {
    //第一种渐变色
    private int mFirstColor;
    //第二种渐变色
    private int mSecondColor;
    //渐变颜色
    private int[] colors;
    //是否开启动画
    boolean isStarted;
    //红色RGB 最大值
    private final static int RED_MAX = 255;
    //绿色RGB 最大值
    private final static int GREEN_MAX = 200;
    // 蓝色RGB 最大值
    private final static int BLUE_MAX = 200;
    //红色RGB 最小值
    private final static int RED_MIN = 0;
    //绿色RGB 最小值
    private final static int GREEN_MIN = 0;
    //蓝色RGB 最小值
    private final static int BLUE_MIN = 100;
    //使用random 随机数值
    private Random random;
    //颜色画笔
    private Paint mPaint;
    //画布
    private RectF mRectF;
    //x0
    private int x0;
    private int y0;
    private int x1;
    private int y1;
    //最终X0的最大长度
    private int mFinalX0;
    //最终Y0的最大高度
    private int mFinalY0;
    //最终X1的最大长度
    private int mFinalX1;
    //最终Y1的最大高度
    private int mFinalY1;

    public GradientView(Context context) {
        super(context);
        init();
    }

    public GradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化参数
     */
    private void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                random = new Random();
                mRectF = new RectF();
                mPaint = new Paint();
                mPaint.setAntiAlias(true);
                mPaint.setDither(true);
                setGradientAngle();
                mFirstColor = Color.parseColor(ColorConvert.toHex16(new int[]{RED_MIN + (random.nextInt(RED_MAX)),
                        GREEN_MIN + (random.nextInt(GREEN_MAX)),
                        BLUE_MIN + (random.nextInt(BLUE_MAX))}));
                mSecondColor = Color.parseColor(ColorConvert.toHex16(new int[]{RED_MIN + (random.nextInt(RED_MAX)),
                        GREEN_MIN + (random.nextInt(GREEN_MAX)),
                        BLUE_MIN + (random.nextInt(BLUE_MAX))}));

                colors = new int[2];
                colors[0] = mFirstColor;
                colors[1] = mSecondColor;

                post(runnable);
            }
        });

    }

    /**
     * 初始化渐变的方向
     * y0 y1 mFinalY0 mFinalY1 初始化在高度的2分之1 位置
     * 因为是设置的是一条直线的渐变 所有高度位置统一不变
     */
    private void setGradientAngle() {
        //x0 在负的2倍控件宽度
        x0 = -getWidth() * 2;
        y0 = getHeight() / 2;

        // x1 初始是0
        x1 = 0;
        y1 = getHeight() / 2;

        //x0 最终结束位置为 控件的宽度
        mFinalX0 = getWidth();
        mFinalY0 = getHeight() / 2;

        //x1 最终结束位置为 控件的2倍宽度
        mFinalX1 = getWidth() * 2;
        mFinalY1 = getHeight() / 2;
    }

    /**
     * 重置颜色和渐变方向
     */
    private void resetColor() {
        //把上一次渐变的第二个颜色赋值给第一个颜色
        mSecondColor = mFirstColor;
        //第一个颜色重新赋值新的颜色
        mFirstColor = Color.parseColor(ColorConvert.toHex16(new int[]{RED_MIN + (random.nextInt(RED_MAX)),
                GREEN_MIN + (random.nextInt(GREEN_MAX)),
                BLUE_MIN + (random.nextInt(BLUE_MAX))}));
        setGradientAngle();
        colors[0] = mFirstColor;
        colors[1] = mSecondColor;
        post(runnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //设置渐变
        Shader shader = new LinearGradient(x0,
                y0,
                x1,
                y1,
                colors,
                null,
                Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        mRectF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRect(mRectF, mPaint);
    }

    /**
     * 使用属性动画来执行
     */
    private void startAnimator() {
        if (isStarted) {
            return;
        }

        PropertyValuesHolder x0PropertyValuesHolder = PropertyValuesHolder.ofInt("x0", x0, mFinalX0);
        PropertyValuesHolder y0PropertyValuesHolder = PropertyValuesHolder.ofInt("y0", y0, mFinalY0);
        PropertyValuesHolder x1PropertyValuesHolder = PropertyValuesHolder.ofInt("x1", x1, mFinalX1);
        PropertyValuesHolder y1PropertyValuesHolder = PropertyValuesHolder.ofInt("y1", y1, mFinalY1);

        ValueAnimator xValueAnimator = ValueAnimator.ofPropertyValuesHolder(x0PropertyValuesHolder, y0PropertyValuesHolder, x1PropertyValuesHolder, y1PropertyValuesHolder);
        xValueAnimator.setDuration(4000 * 3);
        xValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //重新赋值 更新画布
                x0 = (int) animation.getAnimatedValue("x0");
                y0 = (int) animation.getAnimatedValue("y0");
                x1 = (int) animation.getAnimatedValue("x1");
                y1 = (int) animation.getAnimatedValue("y1");

                invalidate();
            }
        });
        xValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //重置
                resetColor();
                isStarted = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        xValueAnimator.start();
        isStarted = true;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startAnimator();
        }
    };
}
