package com.evrencoskun.tableviewsample.tableview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fangyang
 * @since 6/28/14.
 */
public class UIUtil {
    private static final String TAG = UIUtil.class.getSimpleName();

    public static float dipToPix(Context context, float dip) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics);
    }

    public static int dipResToPix(Context context, int dipRes) {
        return context.getResources().getDimensionPixelSize(dipRes);
    }

    private static final Paint measurePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static float textWidth(CharSequence text, int start, int end, float size) {
        measurePaint.setTextSize(size);
        return measurePaint.measureText(text, start, end);
    }

    @SuppressLint("NewApi")
    public static int getWindowWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Point size = new Point();
            display.getSize(size);
            return size.x;
        } else {
            return display.getWidth();
        }
    }

    @SuppressLint("NewApi")
    public static int getWindowHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Point size = new Point();
            display.getSize(size);
            return size.y;
        } else {
            return display.getHeight();
        }
    }


    // To animate view slide out from top to bottom
    public static void slideIn(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, view.getHeight(), 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    // To animate view slide out from bottom to top
    public static void slideOut(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, -view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }


    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int width;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            width = display.getWidth();
        }
        return width;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int height;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            height = size.y;
        } else {
            height = display.getHeight();
        }
        return height;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getContentHeight(Context context) {
        int screenHeight = UIUtil.getScreenHeight(context);
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        int statusBarHeight = UIUtil.getStatusBarHeight(context);
        return screenHeight - statusBarHeight;
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < 17) {
            for (; ; ) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) {
                    newValue = 1; // Roll over to 1, not 0.
                }
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }

    public static Spanned processHighlight(String original, String keyword) {
        Spanned result;
        String originalKeyword = originalKeyword(original, keyword);
        if (TextUtils.isEmpty(original)) {
            return null;
        } else if (!TextUtils.isEmpty(originalKeyword)) {
            result = Html.fromHtml(original.replace(originalKeyword,
                    "<font color=#FF7700>" + originalKeyword + "</font>"));
        } else {
            result = Html.fromHtml(original);
        }
        return result;
    }

    public static String originalKeyword(String str, String searchStr) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(searchStr)) {
            return null;
        }

        final int length = searchStr.length();

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length)) {
                return str.substring(i, i + length);
            }
        }
        return null;
    }

    // navigation bar height
    public static int getNavigationBarHeightInPixelSize(Context context) {
        int navigationBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return navigationBarHeight;
    }

    public static boolean hasNavBar(Resources resources) {
        //Emulator
        if (Build.FINGERPRINT.startsWith("generic"))
            return true;
        if (Build.MODEL.equals("MIX 2")) {
            return false;
        }

        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }

    //超过16/9的长宽比
    public static boolean isFullScreen(@NonNull Context context) {
        int screenWidth = getScreenWidth(context);
        int screenHeight = getScreenHeight(context);
        return ((float) screenHeight) / ((float) screenWidth) > 16f / 9f;
    }


    public static int getAttrColor(int attr, Context context) {
        return getAttrColor(attr, context, context.getTheme(), android.R.color.transparent);
    }

    public static int getAttrColor(int attr, Context context, int defaultColorId) {
        return getAttrColor(attr, context, context.getTheme(), defaultColorId);
    }

    public static int getAttrColor(int attr, Context context, Resources.Theme theme) {
        return getAttrColor(attr, context, theme, android.R.color.transparent);
    }

    public static int getAttrColor(int attr, Context context, Resources.Theme theme, int defaultColorId) {
        TypedArray typedArray = theme.obtainStyledAttributes(new int[]{
                attr
        });
        int color = typedArray.getColor(0, context.getResources().getColor(defaultColorId));
        typedArray.recycle();
        return color;
    }

    public static Drawable getAttrDrawable(int attr, Context context) {
        return getAttrDrawable(attr, context.getTheme());
    }

    public static Drawable getAttrDrawable(int attr, Resources.Theme theme) {
        TypedArray typedArray = theme.obtainStyledAttributes(new int[]{
                attr
        });
        Drawable image = typedArray.getDrawable(0);
        typedArray.recycle();
        return image;
    }

    public static int getAttrResourceId(int attr, Resources.Theme theme) {
        TypedArray typedArray = theme.obtainStyledAttributes(new int[]{
                attr
        });
        int id = typedArray.getResourceId(0, 0);
        typedArray.recycle();
        return id;
    }

}
