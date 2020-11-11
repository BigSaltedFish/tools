package io.ztc.tools;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.fragment.app.Fragment;

import java.util.Objects;

/**
 * <p>Detects Keyboard Status changes and fires events only once for each change.</p>
 */
public class KeyboardStatusDetector {
    private static final int SOFT_KEY_BOARD_MIN_HEIGHT = 100;
    private KeyboardVisibilityListener mVisibilityListener;

    boolean keyboardVisible = false;

    public void registerFragment(Fragment f) {
        registerView(Objects.requireNonNull(f.getView()));
    }

    public void registerActivity(Activity a) {
        registerView(a.getWindow().getDecorView().findViewById(android.R.id.content));
    }

    public KeyboardStatusDetector registerView(final View v) {
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                v.getWindowVisibleDisplayFrame(r);

                int heightDiff = v.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > SOFT_KEY_BOARD_MIN_HEIGHT) { // if more than 100 pixels, its probably a keyboard...
                    if (!keyboardVisible) {
                        keyboardVisible = true;
                        if (mVisibilityListener != null) {
                            mVisibilityListener.onVisibilityChanged(true);
                        }
                    }
                } else {
                    if (keyboardVisible) {
                        keyboardVisible = false;
                        if (mVisibilityListener != null) {
                            mVisibilityListener.onVisibilityChanged(false);
                        }
                    }
                }
            }
        });

        return this;
    }

    public KeyboardStatusDetector setVisibilityListener(KeyboardVisibilityListener listener) {
        mVisibilityListener = listener;
        return this;
    }

    public interface KeyboardVisibilityListener {
        void onVisibilityChanged(boolean keyboardVisible);
    }

}
