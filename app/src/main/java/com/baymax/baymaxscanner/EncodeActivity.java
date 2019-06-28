package com.baymax.baymaxscanner;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.yt.android.zxing.encode.EncodeHelper;

/**
 * @author BayMax·Yi
 * @time 2019/6/19 10:07
 * @modiffy
 * @modiffyTime 2019/6/19 10:07
 * @describe 类描述
 */
public class EncodeActivity extends Activity {
    public static final int TYPE_BARCODE = 1;
    public static final int TYPE_QR = 2;
    public static final String ENCODETYPE = "encode-type";

    ImageView ivQr, ivBarcode, ivResult;
    int type = TYPE_QR;
    String contents;
    EncodeHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);
        type = getIntent().getIntExtra(ENCODETYPE, TYPE_QR);
        contents = getIntent().getStringExtra("data");
        helper = new EncodeHelper();
        initView();
        if (type == TYPE_QR) {
            // createQr();
            createQrLogo();
        } else {
            createBarcode();
        }
    }

    private void createBarcode() {
        Bitmap bitmap = helper.encodeCode(contents, BarcodeFormat.CODE_128, 400, 100);
        if (bitmap == null) return;
        ivResult.setImageBitmap(bitmap);
    }

    private void createQr() {
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                200, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                200, getResources().getDisplayMetrics());
        Bitmap bitmap = helper.encodeQr(contents, width, height);
        if (bitmap == null) return;
        ivResult.setImageBitmap(bitmap);
    }

    private void createQrLogo() {
        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                200, getResources().getDisplayMetrics());
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                200, getResources().getDisplayMetrics());

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = helper.encodeQrLogo(contents, EncodeActivity.this, width, height, R.drawable.icon);
                if (bitmap != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bitmap == null) return;
                            ivResult.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        }).start();


    }

    private void initView() {
        ivBarcode = findViewById(R.id.ivBarcode);
        ivQr = findViewById(R.id.iv_qr);
        switch (type) {
            case TYPE_QR:
                ivResult = ivQr;
                break;
            case TYPE_BARCODE:
                ivResult = ivBarcode;
        }
        ivResult.setVisibility(View.VISIBLE);
    }
}
