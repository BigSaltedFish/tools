package io.ztc.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * 多主题切换工具
 */

public class ThemeUtil {

    public static int getThemeColor(Context context, int attrRes) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{attrRes});
        int color = typedArray.getColor(0, 0xffffff);
        typedArray.recycle();
        return color;
    }

    public static int getCurrentColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.resourceId;
    }

    public static int getCurrentColorPrimaryDark(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.resourceId;
    }

    public static int getCurrentColorAccent(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.resourceId;
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    public static Drawable setTintDrawable(Drawable drawable, Context context) {
        Drawable drawable1 = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(drawable1, context.getResources().getColorStateList(getCurrentColorPrimary(context)));
        return drawable1;
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    public static Drawable setTintDrawable(@DrawableRes int drawable, Context context, @ColorRes int color) {
        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable1 = DrawableCompat.wrap(context.getResources().getDrawable(drawable));
        DrawableCompat.setTintList(drawable1, context.getResources().getColorStateList(color));
        return drawable1;
    }

    public static int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }
}
