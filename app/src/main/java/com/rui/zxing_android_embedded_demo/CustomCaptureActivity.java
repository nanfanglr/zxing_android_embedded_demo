package com.rui.zxing_android_embedded_demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomCaptureActivity extends AppCompatActivity {

    @BindView(R.id.barcode_scanner)
    DecoratedBarcodeView barcodeScanner;
    @BindView(R.id.tv_scan_result)
    TextView tvScanResult;
    @BindView(R.id.buttonsLayout)
    LinearLayout buttonsLayout;
    @BindView(R.id.centerHorizont)
    View centerHorizont;
    @BindView(R.id.barcodePreview)
    ImageView barcodePreview;

    private BeepManager beepManager;
    private String lastText;
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            tvScanResult.setText(result.getText());

            beepManager.playBeepSoundAndVibrate();

            //Added preview of scanned barcode
            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_capture);
        ButterKnife.bind(this);
        barcodeScanner.decodeContinuous(callback);

        beepManager = new BeepManager(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScanner.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeScanner.resume();
    }

    public void pause(View view) {
        barcodeScanner.pause();
    }

    public void resume(View view) {
        barcodeScanner.resume();
    }

    public void triggerScan(View view) {
        barcodeScanner.decodeSingle(callback);
    }
}
