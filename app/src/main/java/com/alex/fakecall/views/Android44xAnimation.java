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
import com.alex.fakecall.utils.ResourceUtils;

public class Android44xAnimation extends View implements Runnable {
    private Paint mPaint;

    private Bitmap[] animFrames;
    private int framePointer = 0;
    private boolean isRunning = false;

    private Bitmap icTouchHandle;
    private Bitmap icAnswerNormal;
    private Bitmap icAnswerActivated;
    private Bitmap icDeclineNormal;
    private Bitmap icDeclineActivated;
    private Bitmap icText;
    private Bitmap icAnimHandle;

    private float baseX;
    private float baseY;
    private float density;
    private float draggable_radius;
    private float xTouchPos;
    private float yTouchPos;
    private float trigger_radius;

    private boolean isDragging = false;
    private int currentTrigger = OnTriggerListener.TRIGGER_NONE;

    private OnTriggerListener listener;

    private Thread thread;

    public Android44xAnimation(Context context) {
        this(context, null);
    }

    public Android44xAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        animFrames = new Bitmap[10];
        animFrames[0] = ResourceUtils.decodeResource(context, R.drawable.android_44x_anim_00);
        animFrames[1] = ResourceUtils.decodeResource(context, R.drawable.android_44x_anim_01);
        animFrames[2] = ResourceUtils.decodeResource(context, R.drawable.android_44x_anim_02);
        animFrames[3] = ResourceUtils.decodeResource(context, R.drawable.android_44x_anim_03);
        animFrames[4] = ResourceUtils.decodeResource(context, R.drawable.android_44x_anim_04);
        animFrames[5] = ResourceUtils.decodeResource(context, R.drawable.android_44x_anim_05);
        animFrames[6] = ResourceUtils.decodeResource(context, R.drawable.android_44x_anim_06);
        animFrames[7] = ResourceUtils.decodeResource(context, R.drawable.android_44x_anim_07);
        animFrames[8] = ResourceUtils.decodeResource(context, R.drawable.android_44x_anim_08);
        animFrames[9] = ResourceUtils.decodeResource(context, R.drawable.android_44x_anim_09);

        icTouchHandle = ResourceUtils.decodeResource(context, R.drawable.android_44x_ic_in_call_touch_handle_normal);
        icDeclineNormal = ResourceUtils.decodeResource(context, R.drawable.android_44x_ic_lockscreen_decline_normal);
        icDeclineActivated = ResourceUtils.decodeResource(context, R.drawable.android_44x_ic_lockscreen_decline_activated);
        icAnswerNormal = ResourceUtils.decodeResource(context, R.drawable.android_44x_ic_lockscreen_answer_normal);
        icAnswerActivated = ResourceUtils.decodeResource(context, R.drawable.android_44x_ic_lockscreen_answer_activated);
        icText = ResourceUtils.decodeResource(context, R.drawable.android_44x_ic_text_holo_dark);
        icAnimHandle = ResourceUtils.decodeResource(context, R.drawable.android_44x_anim_handle);

        density = getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int desiredWidth = getResources().getDisplayMetrics().widthPixels;
        int desiredHeight = (int) (desiredWidth * 1.048f);

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
        baseY = height / 2;
        draggable_radius = (width * 0.69f) / 2.0f;
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

                currentTrigger = OnTriggerListener.TRIGGER_NONE;

                float right = baseX + draggable_radius - trigger_radius;
                float left = baseX - draggable_radius + trigger_radius;
                float top = baseY - draggable_radius + trigger_radius;

                if (xTouchPos < right) {
                    if (xTouchPos > left) {
                        if (yTouchPos > top) {
                            currentTrigger = OnTriggerListener.TRIGGER_NONE;
                            break;
                        }
                        currentTrigger = OnTriggerListener.TRIGGER_TEXT;
                        break;
                    }
                    currentTrigger = OnTriggerListener.TRIGGER_DECLINE;
                    break;
                }
                currentTrigger = OnTriggerListener.TRIGGER_ANSWER;
                break;
            case MotionEvent.ACTION_UP:
                isDragging = false;
                xTouchPos = baseX;
                yTouchPos = baseY;
                if (listener != null)
                    listener.onTrigger(currentTrigger);
                currentTrigger = OnTriggerListener.TRIGGER_NONE;
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
                case OnTriggerListener.TRIGGER_NONE:
                    drawAnimHandle(canvas);
                    break;
                case OnTriggerListener.TRIGGER_ANSWER:
                    drawTriggerAnswer(canvas);
                    break;
                case OnTriggerListener.TRIGGER_DECLINE:
                    drawTriggerDecline(canvas);
                    break;
                case OnTriggerListener.TRIGGER_TEXT:
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

    public void setOnTriggerListener(OnTriggerListener l) {
        listener = l;
    }

    public void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
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
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#9CCF00"));

        float x = baseX + draggable_radius;
        float y = baseY;
        canvas.drawCircle(x, y, trigger_radius, mPaint);
    }

    private void drawTriggerDecline(Canvas canvas) {
        if (!isRunning) return;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#FF3C39"));

        float x = baseX - draggable_radius;
        float y = baseY;
        canvas.drawCircle(x, y, trigger_radius, mPaint);
    }

    private void drawTriggerText(Canvas canvas) {
        if (!isRunning) return;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#9CCF00"));

        float x = baseX;
        float y = baseY - draggable_radius;

        canvas.drawCircle(x, y, trigger_radius, mPaint);
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

        Bitmap bm;
        bm = currentTrigger == OnTriggerListener.TRIGGER_ANSWER ? icAnswerActivated : icAnswerNormal;
        if (bm != null && !bm.isRecycled()) {
            float x = baseX + draggable_radius - bm.getWidth() / 2;
            float y = baseY - bm.getHeight() / 2;
            canvas.drawBitmap(bm, x, y, null);
        }

        bm = currentTrigger == OnTriggerListener.TRIGGER_DECLINE ? icDeclineActivated : icDeclineNormal;
        if (bm != null && !bm.isRecycled()) {
            float x = baseX - draggable_radius - bm.getWidth() / 2;
            float y = baseY - bm.getHeight() / 2;
            canvas.drawBitmap(bm, x, y, null);
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

        if (icAnswerNormal != null && !icAnswerNormal.isRecycled()) {
            icAnswerNormal.recycle();
        }
        icAnswerNormal = null;

        if (icAnswerActivated != null && !icAnswerActivated.isRecycled()) {
            icAnswerActivated.recycle();
        }
        icAnswerActivated = null;

        if (icDeclineNormal != null && !icDeclineNormal.isRecycled()) {
            icDeclineNormal.recycle();
        }
        icDeclineNormal = null;

        if (icDeclineActivated != null && !icDeclineActivated.isRecycled()) {
            icDeclineActivated.recycle();
        }
        icDeclineActivated = null;

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
