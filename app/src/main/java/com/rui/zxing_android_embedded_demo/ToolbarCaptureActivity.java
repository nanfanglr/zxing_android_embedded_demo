package com.rui.zxing_android_embedded_demo;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class ToolbarCaptureActivity extends AppCompatActivity {

    private TextView header;
    private TextView footer;
    private DecoratedBarcodeView barcodeScannerView;
    private CaptureManager capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_capture);
        header = findViewById(R.id.tv_header);
        footer = findViewById(R.id.tv_footer);
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);

        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        final BarcodeView barcodeView = barcodeScannerView.getBarcodeView();
        barcodeView.addStateListener(new CameraPreview.StateListener() {
            @Override
            public void previewSized() {
                Rect framingRect = barcodeView.getFramingRect();
                int width = framingRect.width();
                int height = framingRect.height();
                int paddingBottom = (barcodeScannerView.getHeight() - height) / 2 - 60 - 20;
                Log.i("TAG", "-------->width=" + width);
                Log.i("TAG", "-------->height=" + height);
                Log.i("TAG", "-------->toolbar.getHeight()=" + barcodeScannerView.getHeight());
                TextView statusView = barcodeScannerView.getStatusView();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) statusView.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, paddingBottom);
            }

            @Override
            public void previewStarted() {

            }

            @Override
            public void previewStopped() {

            }

            @Override
            public void cameraError(Exception error) {

            }

            @Override
            public void cameraClosed() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }
}
