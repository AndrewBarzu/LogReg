package com.example.xghos.Wrenchy.helpers_extras;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ScrollableViewPager extends ViewPager {

    public ScrollableViewPager(Context context) {
        super(context);
    }

    public ScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    OnTouchListener mListener;


    public void setListener(OnTouchListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mListener.onTouch();
                break;
            case MotionEvent.ACTION_UP:
                mListener.onTouch();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public interface OnTouchListener {
        public void onTouch();
    }

}
