package com.rui.zxing_android_embedded_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class SimpleUseActivity extends AppCompatActivity {
    public final int CUSTOMIZED_REQUEST_CODE = 666;
    TextView tvScanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_use);
        tvScanResult = findViewById(R.id.tv_scan_result);
        findViewById(R.id.btn_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(SimpleUseActivity.this)
                        .setRequestCode(CUSTOMIZED_REQUEST_CODE)
                        .setCaptureActivity(SimpleCaptureActivity.class)
                        .setBeepEnabled(false)
                        .initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CUSTOMIZED_REQUEST_CODE: {
                Toast.makeText(this, "REQUEST_CODE = " + requestCode, Toast.LENGTH_LONG).show();
                break;
            }
            default:
                break;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        if (result.getContents() == null) {
            tvScanResult.setText("没数据");
        } else {
            tvScanResult.setText(result.getContents());
        }
    }
}
