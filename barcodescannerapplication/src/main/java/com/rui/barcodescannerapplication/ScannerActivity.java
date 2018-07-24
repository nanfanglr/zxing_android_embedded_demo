package com.rui.barcodescannerapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.zxingScannerView)
    ZXingScannerView zxingScannerView;
    @BindView(R.id.tv_footer)
    TextView tvFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);
        zxingScannerView.setAutoFocus(true);
        zxingScannerView.setAspectTolerance(0.5f);
    }

    @Override
    public void onPause() {
        super.onPause();
        zxingScannerView.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        zxingScannerView.setResultHandler(this);
        zxingScannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                zxingScannerView.resumeCameraPreview(ScannerActivity.this);
            }
        }, 2000);
    }

}
