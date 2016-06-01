package com.alex.fakecall.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.fakecall.R;

/**
 * A special widget containing two Sliders and a threshold for each. Moving either slider
 * beyond the threshold will cause the registered
 * OnTriggerListener.onTrigger() to be called with
 * {@link OnTriggerListener#LEFT_HANDLE} or {@link OnTriggerListener#RIGHT_HANDLE} to be called.
 * <p/>
 * Deeply inspired from android SlidingTab internal widget but simplified for our use.
 */
public class SlidingTab extends ViewGroup {
    //Threshold = 2/3 of width
    private static final float TARGET_ZONE = 2.0f / 3.0f;

    //vibrate strength
    private static final long VIBRATE_SHORT = 30;
    private static final long VIBRATE_LONG = 40;

    private Vibrator mVibrator;

    //Screen density, for bitmap displaying
    private float density;

    private boolean triggered = false;
    private boolean tracking = false;
    private float targetZone;

    private Slider leftSlider;
    private Slider rightSlider;
    private Slider currentSlider;

    private int mTextAppearance_SlidingTabNormal;
    private int mTextAppearance_SlidingTabActive;

    private OnTriggerListener onTriggerListener;

    public SlidingTab(Context context) {
        super(context);
    }

    public SlidingTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = getResources().getDisplayMetrics().density;

        leftSlider = new Slider(this, R.drawable.ic_jog_dial_answer, R.drawable.jog_tab_target_green,
                R.drawable.jog_tab_bar_left_answer, R.drawable.jog_tab_left_answer);

        rightSlider = new Slider(this, R.drawable.ic_jog_dial_decline, R.drawable.jog_tab_target_red,
                R.drawable.jog_tab_bar_right_decline, R.drawable.jog_tab_right_decline);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SlidingTab,
                0, 0);
        try {
            mTextAppearance_SlidingTabActive = a.getResourceId(R.styleable.SlidingTab_TextAppearance_SlidingTabNormal,
                    android.R.style.TextAppearance_Medium);

            mTextAppearance_SlidingTabNormal = a.getResourceId(R.styleable.SlidingTab_TextAppearance_SlidingTabActive,
                    android.R.style.TextAppearance_Medium);

            String mLeftSliderText = a.getString(R.styleable.SlidingTab_LeftSliderText);
            leftSlider.setHintText(mLeftSliderText);

            String mRightSliderText = a.getString(R.styleable.SlidingTab_RightSliderText);
            rightSlider.setHintText(mRightSliderText);

        } finally {
            a.recycle();
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        final int leftTabHeight = (int) (density * leftSlider.getTabHeight() + 0.5f);
        final int rightTabHeight = (int) (density * rightSlider.getTabHeight() + 0.5f);
        final int height = Math.max(leftTabHeight, rightTabHeight);

        setMeasuredDimension(widthSpecSize, height);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        final float x = event.getX();
        final float y = event.getY();

        final Rect frame = new Rect();

        View leftHandle = leftSlider.tab;
        leftHandle.getHitRect(frame);
        boolean leftHit = frame.contains((int) x, (int) y);

        View rightHandle = rightSlider.tab;
        rightHandle.getHitRect(frame);
        boolean rightHit = frame.contains((int) x, (int) y);

        if (!tracking && !(leftHit || rightHit)) {
            return false;
        }

        if (action == MotionEvent.ACTION_DOWN) {
            tracking = true;
            triggered = false;
            vibrate(VIBRATE_SHORT);
            if (leftHit) {
                currentSlider = leftSlider;
                targetZone = TARGET_ZONE;
                rightSlider.hide();
            } else {
                currentSlider = rightSlider;
                targetZone = 1.0f - TARGET_ZONE;
                leftSlider.hide();
            }
            currentSlider.setState(Slider.STATE_PRESSED);
            currentSlider.showTarget();
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (tracking) {
            final int action = event.getAction();
            final float x = event.getX();
            final float y = event.getY();
            final View handle = currentSlider.tab;
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    moveHandle(x, y);
                    float target = targetZone * getWidth();
                    boolean targetZoneReached = (currentSlider.equals(leftSlider) ? x > target : x < target);

                    if (!triggered && targetZoneReached) {
                        triggered = true;
                        tracking = false;
                        currentSlider.setState(Slider.STATE_ACTIVE);
                        dispatchTriggerEvent(currentSlider.equals(leftSlider) ? OnTriggerListener.LEFT_HANDLE : OnTriggerListener.RIGHT_HANDLE);
                    }

                    if (y <= handle.getBottom() && y >= handle.getTop()) {
                        break;
                    }
                    // Intentionally fall through - we're outside tracking rectangle
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    tracking = false;
                    triggered = false;
                    resetView();
                    break;
                default:
                    break;
            }
        }

        return tracking || super.onTouchEvent(event);
    }

    private void moveHandle(float x, float y) {
        final View handle = currentSlider.tab;
        final View content = currentSlider.text;

        int deltaX = (int) x - handle.getLeft() - (handle.getWidth() / 2);
        handle.offsetLeftAndRight(deltaX);
        content.offsetLeftAndRight(deltaX);

        invalidate();
    }

    private synchronized void vibrate(long duration) {
        if (mVibrator == null) {
            mVibrator = (android.os.Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        }
        mVibrator.vibrate(duration);
    }

    /**
     * Dispatches a trigger event to listener. Ignored if a listener is not set.
     *
     * @param whichHandle
     *            the handle that triggered the event.
     */
    private void dispatchTriggerEvent(int whichHandle) {
        vibrate(VIBRATE_LONG);
        if (onTriggerListener != null) {
            onTriggerListener.onTrigger(this, whichHandle);
        }
    }

    public void resetView() {
        leftSlider.reset();
        rightSlider.reset();
        recomputeLayout(getLeft(), getTop(), getRight(), getBottom());
    }

    public void recomputeLayout(int l, int t, int r, int b) {
        // Center the widgets in the view
        leftSlider.layout(l, t, r, b, Slider.ALIGN_LEFT);
        rightSlider.layout(l, t, r, b, Slider.ALIGN_RIGHT);
        invalidate();
    }

    /**
     * Registers a callback to be invoked when the user triggers an event.
     *
     * @param listener
     *            the OnDialTriggerListener to attach to this view
     */
    public void setOnTriggerListener(OnTriggerListener listener) {
        onTriggerListener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) {
            return;
        }
        recomputeLayout(l, t, r, b);
    }


    /**
     * A container class for all things pertinent to a Slider
     * A Slider consists of 3 views:
     * <p/>
     * {@link #tab} the tab shown on the screen in the default state.
     * {@link #text} the view is revealed once the user slides the tab out
     * {@link #target} the target view that user must drag the slider over to trigger event.
     */
    static class Slider {
        private final ImageView tab;
        private final TextView text;
        private final ImageView target;

        private SlidingTab parent;

        /**
         * Tab alignment - determines which side the tab should be drawn on
         */
        public static final int ALIGN_LEFT = 0;
        public static final int ALIGN_RIGHT = 1;

        /**
         * States for the view.
         */
        private static final int STATE_NORMAL = 0;
        private static final int STATE_PRESSED = 1;
        private static final int STATE_ACTIVE = 2;


        /**
         * Constructor
         *
         * @param parent   the container view
         * @param iconId   drawable for the tab icon
         * @param targetId drawable for the target
         * @param barId    drawable for the bar
         * @param tabId    drawable for the tab
         */
        Slider(SlidingTab parent, int iconId, int targetId, int barId, int tabId) {
            this.parent = parent;

            //Set up for the tab
            tab = new ImageView(parent.getContext());
            tab.setBackgroundResource(tabId);
            tab.setImageResource(iconId);
            tab.setScaleType(ImageView.ScaleType.CENTER);
            tab.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            // Set up for the text
            text = new TextView(parent.getContext());
            text.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            text.setBackgroundResource(barId);

            if (!parent.isInEditMode()) {
                text.setTextAppearance(parent.getContext(), parent.mTextAppearance_SlidingTabActive);
            }

            // Setup for the target
            target = new ImageView(parent.getContext());
            target.setImageResource(targetId);
            target.setScaleType(ImageView.ScaleType.CENTER);
            target.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            target.setVisibility(View.INVISIBLE);


            // this needs to be first - relies on painter's algorithm
            parent.addView(target);
            parent.addView(tab);
            parent.addView(text);
        }

        private void setResources(int iconId, int targetId, int barId, int tabId) {
            tab.setImageResource(iconId);
            tab.setBackgroundResource(tabId);
            text.setBackgroundResource(barId);
            target.setImageResource(targetId);
        }

        private void setHintText(String str) {
            text.setText(str);
        }

        private void hide() {
            text.setVisibility(View.INVISIBLE);
            tab.setVisibility(View.INVISIBLE);
            target.setVisibility(View.INVISIBLE);
        }

        private void setState(int state) {
            text.setPressed(state == STATE_PRESSED);
            tab.setPressed(state == STATE_PRESSED);

            if (state == STATE_ACTIVE) {
                final int[] activeState = new int[]{android.R.attr.state_active};
                if (text.getBackground().isStateful()) {
                    text.getBackground().setState(activeState);
                }
                if (tab.getBackground().isStateful()) {
                    tab.getBackground().setState(activeState);
                }
                text.setTextAppearance(text.getContext(), parent.mTextAppearance_SlidingTabActive);
            } else {
                text.setTextAppearance(text.getContext(), parent.mTextAppearance_SlidingTabNormal);
            }
        }

        private void showTarget() {
            target.setVisibility(View.VISIBLE);
        }

        private void reset() {
            setState(STATE_NORMAL);
            text.setVisibility(View.VISIBLE);
            text.setTextAppearance(text.getContext(), parent.mTextAppearance_SlidingTabNormal);
            tab.setVisibility(View.VISIBLE);
            target.setVisibility(View.INVISIBLE);
        }

        private void layout(int l, int t, int r, int b, int alignment) {
            final int handleWidth = tab.getBackground().getIntrinsicWidth();
            final int handleHeight = tab.getBackground().getIntrinsicHeight();
            final int targetWidth = target.getDrawable().getIntrinsicWidth();
            final int targetHeight = target.getDrawable().getIntrinsicHeight();
            final int parentWidth = r - l;
            final int parentHeight = b - t;

            final int leftTarget = (int) (TARGET_ZONE * parentWidth) - targetWidth + handleWidth / 2;
            final int rightTarget = (int) ((1.0f - TARGET_ZONE) * parentWidth) - handleWidth / 2;

            final int targetTop = (parentHeight - targetHeight) / 2;
            final int targetBottom = targetTop + targetHeight;
            final int top = (parentHeight - handleHeight) / 2;
            final int bottom = (parentHeight + handleHeight) / 2;
            if (alignment == ALIGN_LEFT) {
                tab.layout(0, top, handleWidth, bottom);
                text.layout(0 - parentWidth, top, 0, bottom);
                text.setGravity(Gravity.END);
                target.layout(leftTarget, targetTop, leftTarget + targetWidth, targetBottom);
            } else {
                tab.layout(parentWidth - handleWidth, top, parentWidth, bottom);
                text.layout(parentWidth, top, parentWidth + parentWidth, bottom);
                target.layout(rightTarget, targetTop, rightTarget + targetWidth, targetBottom);
                text.setGravity(Gravity.TOP);
            }
        }

        public int getTabHeight() {
            return tab.getBackground().getIntrinsicHeight();
        }
    }

    interface OnTriggerListener {
        /**
         * The interface was triggered because the user grabbed the left handle and
         * moved it past the target zone.
         */
        int LEFT_HANDLE = 0;

        /**
         * The interface was triggered because the user grabbed the right handle and
         * moved it past the target zone.
         */
        int RIGHT_HANDLE = 1;

        /**
         * Called when the user moves a handle beyond the target zone.
         *
         * @param v The view that was triggered.
         * @param whichHandle Which "dial handle" the user grabbed, either
         *            {@link #LEFT_HANDLE}, {@link #RIGHT_HANDLE}.
         */
        void onTrigger(View v, int whichHandle);
    }
}
