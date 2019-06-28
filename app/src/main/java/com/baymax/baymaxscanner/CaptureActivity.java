package com.baymax.baymaxscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceView;
import android.view.View;

import com.google.zxing.Result;
import com.yt.android.zxing.callback.OnDecodedResultListener;
import com.yt.android.zxing.camera.PreviewHelper;
import com.yt.android.zxing.view.ViewfinderView;

/**
 * @author BayMax?Yi
 * @time 2019/6/11 13:41
 * @modiffy
 * @modiffyTime 2019/6/11 13:41
 * @describe
 */
public class CaptureActivity extends Activity {
    private SurfaceView surfaceView;
    private ViewfinderView viewfinderView;
    private PreviewHelper previewHelper;

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        initView();
        initPreviewHelper();
        previewHelper.onCreate();
    }

    private void initView() {
        surfaceView = findViewById(R.id.surface_view);
        viewfinderView = findViewById(R.id.viewfinder_view);
        configViewFiderView(viewfinderView);
    }

    private void initPreviewHelper() {
        previewHelper = new PreviewHelper();
        previewHelper.setScanType(getIntent().getIntExtra("type", PreviewHelper.SCANTYPE_QR));
        previewHelper.setActivity(this);
        previewHelper.setSurfaceHolder(surfaceView.getHolder());

        previewHelper.setViewfinderView(viewfinderView);
        previewHelper.setOnDecodedResultListener(onDecodeListener);

    }

    private void configViewFiderView(ViewfinderView viewfinderView) {
        viewfinderView.setAnimatorDuretion(1500);
        viewfinderView.setConorColor(Color.GREEN);
        viewfinderView.setCornorLength(60);
        viewfinderView.setLineColors(new int[]{Color.argb(10, 155, 255, 60),
                Color.argb(30, 155, 255, 60),
                Color.argb(50, 155, 255, 60),
                Color.argb(70, 155, 255, 60),
                Color.argb(90, 155, 255, 60),
                Color.argb(70, 155, 255, 60),
                Color.argb(50, 155, 255, 60),
                Color.argb(30, 155, 255, 60),
                Color.argb(10, 155, 255, 60),
                Color.argb(5, 155, 255, 60),});
        viewfinderView.setLinePercents(new float[]{0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1f});
    }

    @Override
    protected void onResume() {
        super.onResume();
        previewHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        previewHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        previewHelper.onDestroy();
    }

    private OnDecodedResultListener onDecodeListener = new OnDecodedResultListener() {
        @Override
        public void onDecodedResult(Result rawResult) {
            previewHelper.beepAndViravle();
            Intent intent = new Intent();
            intent.putExtra("result", rawResult.getText());
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    public void openLight(View view) {
        previewHelper.openTorch();
    }

    public void closeLight(View view) {
        previewHelper.closeTorch();
    }
}
