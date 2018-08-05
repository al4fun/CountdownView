package cn.szx.countdownviewsample;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class CountdownView extends View {
    private int width = 200, height = 200;//控件的宽高
    private int strokeWidth = 4;//圆圈宽度
    private int textSize = 48;//文字尺寸
    private RectF rectF;

    ValueAnimator valueAnimator;

    private int duration;//倒计时持续时间，秒
    private String timeRemians;//剩余时间，秒。用于展示
    private int currentDegree;

    private Paint backgroundPaint, forgroundPaint, textPaint;
    private int backgroundColor = Color.parseColor("#DDDDDD");
    private int forgroundColor = Color.parseColor("#FE872E");
    private int textColor = Color.parseColor("#FE872E");
    private Rect textBounds = new Rect();

    private Listener listener;

    public interface Listener {
        void onFinished();
    }

    public CountdownView(Context context, int duration, @Nullable Listener listener) {
        super(context);
        this.duration = duration;
        this.listener = listener;

        initPaint();
        initTime();
    }

    private void initPaint() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setAntiAlias(true);

        forgroundPaint = new Paint();
        forgroundPaint.setColor(forgroundColor);
        forgroundPaint.setStyle(Paint.Style.STROKE);
        forgroundPaint.setStrokeWidth(strokeWidth);
        forgroundPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
    }

    private void initTime() {
        timeRemians = String.valueOf(duration);
        textPaint.getTextBounds(timeRemians, 0, timeRemians.length(), textBounds);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (rectF == null) {
            rectF = new RectF(strokeWidth / 2, strokeWidth / 2, getWidth() - strokeWidth / 2, getHeight() - strokeWidth / 2);
        }

        //canvas.drawColor(Color.parseColor("#FFFFFF"));
        canvas.drawArc(rectF, 270, 360, false, backgroundPaint);
        canvas.drawArc(rectF, 270, currentDegree, false, forgroundPaint);
        canvas.drawText(timeRemians, getWidth() / 2, getHeight() / 2 + textBounds.height() / 2, textPaint);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        if (hasWindowFocus) {//所在窗口获得焦点
            if (valueAnimator == null) {
                valueAnimator = ValueAnimator.ofInt(0, 360);//圆弧角度从0到360度
                valueAnimator.setDuration(duration * 1000);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int degree = (int) valueAnimator.getAnimatedValue();
                        //if (degree % 2 == 0) {//每变化两度才更新一次
                        currentDegree = degree;
                        timeRemians = String.valueOf(duration - valueAnimator.getCurrentPlayTime() / 1000);
                        textPaint.getTextBounds(timeRemians, 0, timeRemians.length(), textBounds);
                        postInvalidate();
                        //}

                        if (degree == 360 && listener != null) {
                            listener.onFinished();
                        }
                    }
                });
            }
            valueAnimator.start();
        } else {//所在窗口失去焦点
            valueAnimator.cancel();
        }
    }

//--------------------------------------------------------------------------------------------------

    public CountdownView setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public CountdownView setTextsize(int textSize) {
        this.textSize = textSize;
        initPaint();
        return this;
    }

    public CountdownView setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        initPaint();
        return this;
    }

    public CountdownView setColor(int backgroundColor, int forgroundColor, int textColor) {
        this.backgroundColor = backgroundColor;
        this.forgroundColor = forgroundColor;
        this.textColor = textColor;
        initPaint();
        return this;
    }
}