package com.example.gamie.adapters;

import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

public class GamesGridGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    public interface OnGesture {
        void leftSwipe();
        void rightSwipe();
        void topSwipe();
        void bottomSwipe();
    }

    public GamesGridGestureListener(@Nullable OnGesture onGesture) {
        this.onGesture = onGesture;
    }

    private OnGesture onGesture;

    public void setOnGesture(OnGesture onGesture) {
        this.onGesture = onGesture;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {

        float diffY = event2.getY() - event1.getY();
        float diffX = event2.getX() - event1.getX();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    if (onGesture != null) {
                        onGesture.rightSwipe();
                    }
                } else {
                    if (onGesture != null) {
                        onGesture.leftSwipe();
                    }
                }
            }
        } else {
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    if (onGesture != null) {
                        onGesture.bottomSwipe();
                    }
                } else {
                    if (onGesture != null) {
                        onGesture.topSwipe();
                    }
                }
            }
        }
        return true;
    }
}
