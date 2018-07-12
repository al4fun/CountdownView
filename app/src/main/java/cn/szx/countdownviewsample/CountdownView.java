package cn.szx.countdownviewsample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.view.View;

public class CountdownView extends View {
    private int width = 200, height = 200;//控件的宽高
    private int strokeWidth = 4;//圆圈宽度
    private int textSize = 48;//文字尺寸

    private int duration;//倒计时持续时间，秒
    private long timeRemainsMili;//剩余时间，毫秒
    private String timeRemians;//剩余时间，秒。用于展示
    private long step;
    private int currentDegree = 0;

    private Paint backgroundPaint, forgroundPaint, textPaint;
    private int backgroundColor = Color.parseColor("#DDDDDD");
    private int forgroundColor = Color.parseColor("#FE872E");
    private int textColor = Color.parseColor("#FE872E");
    private Rect textBounds = new Rect();

    private boolean finished = false;
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
        timeRemainsMili = duration * 1000;
        timeRemians = String.valueOf(duration);
        textPaint.getTextBounds(timeRemians, 0, timeRemians.length(), textBounds);
        step = duration * 1000 / 180;//圆弧每旋转2度，大约需要经过的时间间隔(毫秒)。一周360度
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawColor(Color.parseColor("#FFFFFF"));

        RectF rectF = new RectF(0 + strokeWidth / 2, 0 + strokeWidth / 2, getWidth() - strokeWidth / 2, getHeight() - strokeWidth / 2);
        canvas.drawArc(rectF, 270, 360, false, backgroundPaint);
        canvas.drawArc(rectF, 270, currentDegree, false, forgroundPaint);
        canvas.drawText(timeRemians, getWidth() / 2, getHeight() / 2 + textBounds.height() / 2, textPaint);

        if (currentDegree < 360) {
            currentDegree += 2;
            timeRemainsMili -= step;
            timeRemians = String.valueOf(timeRemainsMili / 1000 + 1);
            textPaint.getTextBounds(timeRemians, 0, timeRemians.length(), textBounds);
            postInvalidateDelayed(step);
        } else {
            if (!finished) {
                finished = true;
                timeRemians = "0";
                invalidate();
                if (listener != null) {
                    listener.onFinished();
                }
            }
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