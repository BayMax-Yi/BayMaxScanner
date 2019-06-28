package com.baymax.baymaxscanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.yt.android.zxing.camera.PreviewHelper;

/**
 * @author BayMax·Yi
 * @time 2019/6/18 14:45
 * @modiffy
 * @modiffyTime 2019/6/18 14:45
 * @describe 类描述
 */
public class MainActivity extends Activity {
    private static final int REQUEST_CODE_PERMIISON = 101;
    private TextView tvResult;
    private String[] permissions = {
            Manifest.permission.CAMERA

    };
    private int type = PreviewHelper.SCANTYPE_QR;
    private final String CONTENT = "haha123123132";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.tvResult);
    }

    public void scanQr(View view) {
        type = PreviewHelper.SCANTYPE_QR;
        checkPermission();
    }

    public void scanBarcode(View view) {
        type = PreviewHelper.SCANTYPE_BARCODE;
        checkPermission();

    }

    public void createBarCode(View view) {
        Intent intent = new Intent(this, EncodeActivity.class);
        intent.putExtra(EncodeActivity.ENCODETYPE, EncodeActivity.TYPE_BARCODE);

        intent.putExtra("data", CONTENT);
        startActivity(intent);
    }

    public void createQr(View view) {
        Intent intent = new Intent(this, EncodeActivity.class);
        intent.putExtra(EncodeActivity.ENCODETYPE, EncodeActivity.TYPE_QR);
        intent.putExtra("data", CONTENT);
        startActivity(intent);
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            onPermissionGranted();
            return;
        }

        boolean hasPermission = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false;
                break;
            }
        }

        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMIISON);
        } else {
            onPermissionGranted();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");
            tvResult.setText(result);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMIISON) {
            boolean hasPermission = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    hasPermission = false;
                }
            }
            if (hasPermission)
                onPermissionGranted();
        }
    }

    private void onPermissionGranted() {
        Intent intent = new Intent(this, CaptureActivity.class);
        intent.putExtra("type", type);
        startActivityForResult(intent, 1);
    }
}
