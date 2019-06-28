/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yt.android.zxing.camera;

import com.google.zxing.Result;
import com.yt.android.zxing.hardware.AmbientLightManager;
import com.yt.android.zxing.hardware.BeepManager;
import com.yt.android.zxing.decod.CaptureActivityHandler;
import com.yt.android.zxing.hardware.GravitySensorManager;
import com.yt.android.zxing.utils.InactivityTimer;
import com.yt.android.zxing.callback.OnDecodedResultListener;
import com.yt.android.zxing.view.ViewfinderView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public class PreviewHelper implements SurfaceHolder.Callback {

    private static final String TAG = PreviewHelper.class.getSimpleName();


    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private int orientation = 0;

    private Activity activity;


    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private AmbientLightManager ambientLightManager;
    private SurfaceHolder surfaceHolder;

    private OnDecodedResultListener onDecodedResultListener;
    private int scanType;
    public final static int SCANTYPE_QR = 1;
    public final static int SCANTYPE_BARCODE = 2;


    public void onCreate() {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(activity);
        beepManager = new BeepManager(activity);
        ambientLightManager = new AmbientLightManager(activity);
        initOrientationListener();
    }

    private void initOrientationListener() {
        if (scanType == SCANTYPE_QR) {
            return;
        }
        GravitySensorManager.getInstance(activity).setIsAutoRotate(false)
                .setGravitySensorListener(new GravitySensorManager.GravitySensorListener() {
                    @Override
                    public void onLandscape() {
                        if (handler != null) handler.setOrientation(0);
                    }

                    @Override
                    public void onFlipLandscape() {
                        if (handler != null) handler.setOrientation(0);
                    }

                    @Override
                    public void onPortpait() {
                        if (handler != null) handler.setOrientation(90);
                    }

                    @Override
                    public void onFlipPortpait() {
                        if (handler != null) handler.setOrientation(90);
                    }
                });
    }


    public void onResume() {
        cameraManager = new CameraManager(activity.getApplication());
        viewfinderView.setCameraManager(cameraManager);
        handler = null;
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
        }
    }

    public void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        ambientLightManager.stop();
        beepManager.close();
        cameraManager.closeDriver();
        if (!hasSurface) {
            if (surfaceHolder != null) surfaceHolder.removeCallback(this);
        }
    }

    public void onDestroy() {
        inactivityTimer.shutdown();
        if (viewfinderView != null) {
            viewfinderView.destoryAnimator();
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        if (onDecodedResultListener != null) {
            onDecodedResultListener.onDecodedResult(rawResult);
        }
    }


    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {

            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, null, null, null, cameraManager);
                handler.setOrientation(orientation);
            }
        } catch (IOException ioe) {

        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
        }
    }


    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }


    public void beepAndViravle() {
        beepManager.playBeepSoundAndVibrate();
    }

    public void openTorch() {
        cameraManager.setTorch(true);
    }

    public void closeTorch() {
        cameraManager.setTorch(false);
    }

    public final void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    public void setViewfinderView(ViewfinderView viewfinderView) {
        this.viewfinderView = viewfinderView;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public void setOnDecodedResultListener(OnDecodedResultListener onDecodedResultListener) {
        this.onDecodedResultListener = onDecodedResultListener;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setScanType(int scanType) {
        this.scanType = scanType;
    }
}
