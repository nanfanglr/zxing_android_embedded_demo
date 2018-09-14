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

package com.rui.zxing_android_embedded_demo.scanview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.ViewfinderView;
import com.rui.zxing_android_embedded_demo.R;

import java.util.List;

/**
 * 通过继承重写了ondraw方法，添加了扫描边框
 */
public class MyViewfinderView extends ViewfinderView {

    protected final Paint paintCorner;
    protected final int rectCornerColor;
    private final int rectOutColor;
    protected int frameLength;
    protected int frameWidth;


    // This constructor is used when the class is built from an XML resource.
    public MyViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintCorner = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.rectCornerColor = getResources().getColor(R.color.rectCornerColor);
        this.rectOutColor = getResources().getColor(R.color.rectOutColor);
    }


    @Override
    public void onDraw(Canvas canvas) {
        refreshSizes();
        if (framingRect == null || previewFramingRect == null) {
            return;
        }

        final Rect frame = framingRect;
        final Rect previewFrame = previewFramingRect;

        final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        // Draw frame rect corner
        paintCorner.setColor(rectOutColor);
        //frame rect corner
//        frameLength = (int) (32 * getResources().getDisplayMetrics().density + 0.5f);
        frameWidth = (int) (3 * getResources().getDisplayMetrics().density + 0.5f);
        canvas.drawRect(frame.left, frame.top, frame.right, frame.top + frameWidth, paintCorner);
        canvas.drawRect(frame.left, frame.top, frame.left + frameWidth, frame.bottom, paintCorner);
        canvas.drawRect(frame.left, frame.bottom - frameWidth, frame.right, frame.bottom, paintCorner);
        canvas.drawRect(frame.right - frameWidth, frame.top, frame.right, frame.bottom, paintCorner);
        paintCorner.setColor(rectCornerColor);
        frameLength = (int) (frame.width() / 20 + 0.5f);
        canvas.drawRect(frame.left, frame.top, frame.left + frameLength, frame.top + frameWidth, paintCorner);
        canvas.drawRect(frame.left, frame.top, frame.left + frameWidth, frame.top + frameLength, paintCorner);
        canvas.drawRect(frame.left, frame.bottom - frameLength, frame.left + frameWidth, frame.bottom, paintCorner);
        canvas.drawRect(frame.left, frame.bottom - frameWidth, frame.left + frameLength, frame.bottom, paintCorner);
        canvas.drawRect(frame.right - frameWidth, frame.bottom - frameLength, frame.right, frame.bottom, paintCorner);
        canvas.drawRect(frame.right - frameLength, frame.bottom - frameWidth, frame.right, frame.bottom, paintCorner);
        canvas.drawRect(frame.right - frameWidth, frame.top, frame.right, frame.top + frameLength, paintCorner);
        canvas.drawRect(frame.right - frameLength, frame.top, frame.right, frame.top + frameWidth, paintCorner);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, frame, paint);
        } else {

            // Draw a red "laser scanner" line through the middle to show decoding is active
            paint.setColor(laserColor);
            paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
            final int middle = frame.height() / 2 + frame.top;
            canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);

            final float scaleX = frame.width() / (float) previewFrame.width();
            final float scaleY = frame.height() / (float) previewFrame.height();

            final int frameLeft = frame.left;
            final int frameTop = frame.top;

            // draw the last possible result points
            if (!lastPossibleResultPoints.isEmpty()) {
                paint.setAlpha(CURRENT_POINT_OPACITY / 2);
                paint.setColor(resultPointColor);
                float radius = POINT_SIZE / 2.0f;
                for (final ResultPoint point : lastPossibleResultPoints) {
                    canvas.drawCircle(
                            frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            radius, paint
                    );
                }
                lastPossibleResultPoints.clear();
            }

            // draw current possible result points
            if (!possibleResultPoints.isEmpty()) {
                paint.setAlpha(CURRENT_POINT_OPACITY);
                paint.setColor(resultPointColor);
                for (final ResultPoint point : possibleResultPoints) {
                    canvas.drawCircle(
                            frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            POINT_SIZE, paint
                    );
                }

                // swap and clear buffers
                final List<ResultPoint> temp = possibleResultPoints;
                possibleResultPoints = lastPossibleResultPoints;
                lastPossibleResultPoints = temp;
                possibleResultPoints.clear();
            }

            // Request another update at the animation interval, but only repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY,
                    frame.left - POINT_SIZE,
                    frame.top - POINT_SIZE,
                    frame.right + POINT_SIZE,
                    frame.bottom + POINT_SIZE);
        }
    }


}
