package com.yt.android.zxing.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

/**
 * @author BayMax·Yi
 * @time 2019/6/19 11:17
 * @modiffy
 * @modiffyTime 2019/6/19 11:17
 * @describe 类描述
 */
public class BitmapUtils {
    public static Bitmap decodeBitmapResource(Context context, int res, int viewWidth, int viewHeight) {
        float scale = caculateScale(context, res, viewWidth, viewHeight);
        if (scale < 1)
            return BitmapFactory.decodeResource(context.getResources(), res, null);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        scale = (float) Math.ceil(scale);
        options.inSampleSize = (int) scale;
        return BitmapFactory.decodeResource(context.getResources(), res, options);
    }

    private static float caculateScale(Context context, int res, int viewWidth, int viewHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), res, options);
        int width = options.outWidth;
        int height = options.outHeight;
        return Math.min(width * 1.0f / viewWidth, height * 1.0f / viewHeight);
    }
}
