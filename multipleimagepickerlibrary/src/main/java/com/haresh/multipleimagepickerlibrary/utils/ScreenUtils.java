package com.haresh.multipleimagepickerlibrary.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;


public class ScreenUtils {

    public static Point getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point out = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(out);
        } else {
            int width = display.getWidth();
            int height = display.getHeight();
            out.set(width, height);
        }
        return out;
    }
}
