package com.yt.android.zxing.hardware;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by luke on 2017/9/26.
 */

public class GravitySensorManager implements SensorEventListener {

    private static GravitySensorManager gravitySensorManager;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private static Context mContext;

    public Calendar mCalendar;

    public boolean mIsAutoRotate = true;

    long time = -1;

    public GravitySensorListener gsl;

    int mStat = 0;
    int stat = 0;

    String TAG = "GravitySensorManager";

    private GravitySensorManager(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY

        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public static GravitySensorManager getInstance(Context context) {
        mContext = context;
        if (gravitySensorManager == null) {
            gravitySensorManager = new GravitySensorManager(mContext);
        }
        return gravitySensorManager;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];
           // Log.e(TAG, "onSensorChanged: "+x );
            mCalendar = Calendar.getInstance();

            int second = mCalendar.get(Calendar.SECOND);// 53

            if (time != second) {
                if (x != y) {
                    if (Math.abs(x) > Math.abs(y)) {
                        if (x == 0) {
                            return;
                        } else {
                            if (x < 0) {
                                stat = 1;

                            } else {
                                stat = 2;
                            }
                        }
                    } else {
                        if (y == 0) {
                            return;
                        } else {
                            if (y > 0) {
                                stat = 3;

                            } else {
                                stat = 4;
                            }
                        }
                    }
                }
            }
            time = second;
        }

        if (stat != mStat) {
            mStat = stat;
            Log.e(TAG, "stat: " + stat);
            switch (stat) {
                case 1:
                    if (mIsAutoRotate) {
                        ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    }
                    Log.d(TAG, "onLandscape");
                    gsl.onLandscape();
                    break;
                case 2:
                    if (mIsAutoRotate) {
                        ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    }
                    Log.d(TAG, "onFlipLandscape");
                    gsl.onFlipLandscape();
                    break;
                case 3:
                    if (mIsAutoRotate) {
                        ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    }
                    Log.d(TAG, "onPortpait");
                    gsl.onPortpait();
                    break;
                case 4:
                    if (mIsAutoRotate) {
                        ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    }
                    Log.d(TAG, "onFlipPortpait");
                    gsl.onFlipPortpait();
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public GravitySensorManager setGravitySensorListener(GravitySensorListener gsl) {
        this.gsl = gsl;
        return getInstance(mContext);
    }

    //监听器
    public interface GravitySensorListener {
        //横屏
        void onLandscape();

        //反转横屏
        void onFlipLandscape();

        //竖屏
        void onPortpait();

        //反转竖屏
        void onFlipPortpait();
    }

    public GravitySensorManager setIsAutoRotate(boolean isAutoRotate) {
        mIsAutoRotate = isAutoRotate;
        return getInstance(mContext);
    }
}
