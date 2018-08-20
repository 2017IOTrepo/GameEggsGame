package com.example.a41448.huawu.Face;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.vise.log.ViseLog;

/**
 * @Description: 绘制人脸识别框
 */
public class FaceRectView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private int mRectColor;
    private boolean mFaceIsRect;

    public FaceRectView(Context context) {
        super(context);
        init(context);
    }

    public FaceRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FaceRectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public FaceRectView setFaceIsRect(boolean mFaceIsRect) {
        this.mFaceIsRect = mFaceIsRect;
        return this;
    }

    public FaceRectView setRectColor(int mRectColor) {
        this.mRectColor = mRectColor;
        return this;
    }

    /**
     * 绘制人脸识别框
     *
     * @param mDetectorData
     */
    public <T> void drawFaceRect(DetectorData<T> mDetectorData) {
        Canvas canvas = mHolder.lockCanvas();
        if (null == canvas) {
            return;
        }

        if (mDetectorData == null || mDetectorData.getFaceRectList() == null) {
            mHolder.unlockCanvasAndPost(canvas);
            return;
        }

        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        if (mDetectorData.getFaceRectList() != null && mDetectorData.getFaceRectList().length > 0) {
            for (Rect rect : mDetectorData.getFaceRectList()) {
                FaceUtil.drawFaceRect(canvas, rect, mRectColor, mFaceIsRect);
            }
        } else {
            ViseLog.d("faces:0");
        }

        mHolder.unlockCanvasAndPost(canvas);
    }
}
