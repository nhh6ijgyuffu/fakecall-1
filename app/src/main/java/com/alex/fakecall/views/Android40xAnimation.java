package com.alex.fakecall.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.alex.fakecall.R;
import com.alex.fakecall.helper.Utils;


public class Android40xAnimation extends View implements Runnable{
    private Paint mPaint;

    private Bitmap[] animFrames;
    private int framePointer = 0;
    private boolean isRunning = false;

    private Bitmap icTouchHandle;
    private Bitmap icAnswer;
    private Bitmap icDecline;
    private Bitmap icText;
    private Bitmap icAnimHandle;

    private float baseX;
    private float baseY;
    private float draggable_radius;
    private float xTouchPos;
    private float yTouchPos;
    private float trigger_radius;
    private float density;

    private boolean isDragging = false;
    private int currentTrigger = CallAnimOnTriggerListener.TRIGGER_NONE;

    private CallAnimOnTriggerListener listener;

    private Thread thread;

    public Android40xAnimation(Context context) {
        this(context, null);
    }

    public Android40xAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        animFrames = new Bitmap[5];
        animFrames[0] = Utils.decodeResource(getContext(), R.drawable.android_40x_anim_0);
        animFrames[1] = Utils.decodeResource(getContext(), R.drawable.android_40x_anim_1);
        animFrames[2] = Utils.decodeResource(getContext(), R.drawable.android_40x_anim_2);
        animFrames[3] = Utils.decodeResource(getContext(), R.drawable.android_40x_anim_3);
        animFrames[4] = Utils.decodeResource(getContext(), R.drawable.android_40x_anim_4);

        icTouchHandle = Utils.decodeResource(getContext(), R.drawable.android_44x_ic_in_call_touch_handle_normal);
        icDecline = Utils.decodeResource(context, R.drawable.android_44x_ic_lockscreen_decline_normal);
        icAnswer = Utils.decodeResource(context, R.drawable.android_44x_ic_lockscreen_answer_normal);
        icText = Utils.decodeResource(context, R.drawable.android_44x_ic_text_holo_dark);
        icAnimHandle = Utils.decodeResource(context, R.drawable.android_40x_ic_lockscreen_handle_pressed);

        density = getResources().getDisplayMetrics().density;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int desiredWidth = getResources().getDisplayMetrics().widthPixels;
        int desiredHeight = (int) ((getResources().getDisplayMetrics().heightPixels * 420) / 800.0f);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);

        //int variables depended on size
        baseX = width / 2;
        baseY = (height + 80 * density) / 2.0f;
        draggable_radius = ((width * 400) / 480.0f) / 2.0f;
        trigger_radius = 47.0f * density;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isDragging = true;
                xTouchPos = event.getX();
                yTouchPos = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                isDragging = true;
                xTouchPos = event.getX();
                yTouchPos = event.getY();

                currentTrigger = CallAnimOnTriggerListener.TRIGGER_NONE;

                float right = baseX + draggable_radius - trigger_radius;
                float left = baseX - draggable_radius + trigger_radius;
                float top = baseY - draggable_radius + trigger_radius;

                if (xTouchPos < right) {
                    if (xTouchPos > left) {
                        if (yTouchPos > top) {
                            currentTrigger = CallAnimOnTriggerListener.TRIGGER_NONE;
                            break;
                        }
                        currentTrigger = CallAnimOnTriggerListener.TRIGGER_TEXT;
                        break;
                    }
                    currentTrigger = CallAnimOnTriggerListener.TRIGGER_DECLINE;
                    break;
                }
                currentTrigger = CallAnimOnTriggerListener.TRIGGER_ANSWER;
                break;
            case MotionEvent.ACTION_UP:
                isDragging = false;
                xTouchPos = baseX;
                yTouchPos = baseY;
                if (listener != null)
                    listener.onTrigger(currentTrigger);
                currentTrigger = CallAnimOnTriggerListener.TRIGGER_NONE;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas);

        if (isDragging) {
            drawBorder(canvas);
            switch (currentTrigger) {
                case CallAnimOnTriggerListener.TRIGGER_NONE:
                    drawAnimHandle(canvas);
                    break;
                case CallAnimOnTriggerListener.TRIGGER_ANSWER:
                    drawTriggerAnswer(canvas);
                    break;
                case CallAnimOnTriggerListener.TRIGGER_DECLINE:
                    drawTriggerDecline(canvas);
                    break;
                case CallAnimOnTriggerListener.TRIGGER_TEXT:
                    drawTriggerText(canvas);
                    break;
            }
            drawIcons(canvas);
            return;
        }
        drawAnimations(canvas);
        drawTouchHandle(canvas);
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                framePointer++;
                if (framePointer >= animFrames.length)
                    framePointer = 0;
                Thread.sleep(150);
                postInvalidate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnTriggerListener(CallAnimOnTriggerListener l) {
        listener = l;
    }

    public void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    private void drawRect(Canvas canvas) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), mPaint);
    }

    private void drawAnimHandle(Canvas canvas) {
        if (!isRunning) return;
        if (icAnimHandle != null && !icAnimHandle.isRecycled()) {
            float x = xTouchPos - icAnimHandle.getWidth() / 2;
            float y = yTouchPos - icAnimHandle.getHeight() / 2;
            canvas.drawBitmap(icAnimHandle, x, y, null);
        }
    }

    private void drawTriggerAnswer(Canvas canvas) {
        if (!isRunning) return;
        if (icAnimHandle != null && !icAnimHandle.isRecycled()) {
            float x = baseX + draggable_radius - icAnimHandle.getWidth() / 2;
            float y = baseY - icAnimHandle.getHeight() / 2;
            canvas.drawBitmap(icAnimHandle, x, y, null);
        }
    }

    private void drawTriggerDecline(Canvas canvas) {
        if (!isRunning) return;
        if (icAnimHandle != null && !icAnimHandle.isRecycled()) {
            float x = baseX - draggable_radius - icAnimHandle.getHeight() / 2;
            float y = baseY - icAnimHandle.getWidth() / 2;
            canvas.drawBitmap(icAnimHandle, x, y, null);
        }
    }

    private void drawTriggerText(Canvas canvas) {
        if (!isRunning) return;
        if (icAnimHandle != null && !icAnimHandle.isRecycled()) {
            float x = baseX - icAnimHandle.getWidth() / 2;
            float y = baseY - draggable_radius - icAnimHandle.getHeight() / 2;
            canvas.drawBitmap(icAnimHandle, x, y, null);
        }
    }

    private void drawBorder(Canvas canvas) {
        if (!isRunning) return;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2.0f * density);
        mPaint.setColor(Color.parseColor("#181818"));
        canvas.drawCircle(baseX, baseY, draggable_radius, mPaint);
    }

    private void drawIcons(Canvas canvas) {
        if (!isRunning) return;

        if (icText != null && !icText.isRecycled()) {
            float x = baseX - icText.getWidth() / 2;
            float y = baseY - draggable_radius - icText.getWidth() / 2;
            canvas.drawBitmap(icText, x, y, null);
        }

        if (icAnswer != null && !icAnswer.isRecycled()) {
            float x = baseX + draggable_radius - icAnswer.getWidth() / 2;
            float y = baseY - icAnswer.getHeight() / 2;
            canvas.drawBitmap(icAnswer, x, y, null);
        }

        if (icDecline != null && !icDecline.isRecycled()) {
            float x = baseX - draggable_radius - icDecline.getWidth() / 2;
            float y = baseY - icDecline.getHeight() / 2;
            canvas.drawBitmap(icDecline, x, y, null);
        }
    }

    private void drawAnimations(Canvas canvas) {
        if (!isRunning) return;
        if (framePointer >= 0 && framePointer < animFrames.length) {
            Bitmap frame = animFrames[framePointer];
            if (frame != null && !frame.isRecycled()) {
                float x = baseX - frame.getWidth() / 2;
                float y = baseY - frame.getHeight() / 2;
                canvas.drawBitmap(frame, x, y, null);
            }
        }
    }

    private void drawTouchHandle(Canvas canvas) {
        if (!isRunning) return;
        if (icTouchHandle != null && !icTouchHandle.isRecycled()) {
            float x = baseX - icTouchHandle.getWidth() / 2;
            float y = baseY - icTouchHandle.getHeight() / 2;
            canvas.drawBitmap(icTouchHandle, x, y, null);
        }
    }

    public void stop() {
        isRunning = false;
        if(thread != null && !thread.isInterrupted()){
            thread.interrupt();
        }
        thread = null;

        if (icTouchHandle != null && !icTouchHandle.isRecycled()) {
            icTouchHandle.recycle();
        }
        icTouchHandle = null;

        if (icAnimHandle != null && !icAnimHandle.isRecycled()) {
            icAnimHandle.recycle();
        }
        icAnimHandle = null;

        if (icAnswer != null && !icAnswer.isRecycled()) {
            icAnswer.recycle();
        }
        icAnswer = null;

        if (icDecline != null && !icDecline.isRecycled()) {
            icDecline.recycle();
        }
        icDecline = null;

        if (icText != null && !icText.isRecycled()) {
            icText.recycle();
        }
        icText = null;

        for (int i = 0; i < animFrames.length; i++) {
            if (animFrames[i] != null && !animFrames[i].isRecycled())
                animFrames[i].recycle();
            animFrames[i] = null;
        }
        animFrames = null;
    }
}
