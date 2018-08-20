package com.example.a41448.huawu.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a41448.huawu.Face.CameraPreview;
import com.example.a41448.huawu.Face.DetectorData;
import com.example.a41448.huawu.Face.DetectorProxy;
import com.example.a41448.huawu.Face.FaceRectView;
import com.example.a41448.huawu.Face.ICameraCheckListener;
import com.example.a41448.huawu.Face.IDataListener;
import com.example.a41448.huawu.R;
import com.vise.log.ViseLog;
import com.vise.utils.assist.StringUtil;
import com.vise.utils.view.ViewUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FaceDetectorActivity extends Activity {


    private CameraPreview mFace_detector_preview;
    private FaceRectView mFace_detector_face;
    private Button mFace_detector_take_photo;
    private TextView mFace_detector_distance;

    private Context mContext;
    private DetectorProxy mDetectorProxy;
    private DetectorData mDetectorData;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mDetectorData != null) {
                mFace_detector_distance.setText( StringUtil.parseStr(mDetectorData.getLightIntensity()));
            }
        }
    };

    private ICameraCheckListener mCameraCheckListener = new ICameraCheckListener() {
        @Override
        public void checkPermission(boolean isAllow) {
            ViseLog.i("checkPermission" + isAllow);
            if (!isAllow) {
                Toast.makeText(mContext, "权限申请被拒绝，请进入设置打开权限再试！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public void checkPixels(long pixels, boolean isSupport) {
            ViseLog.i("checkPixels" + pixels);
            if (!isSupport) {
                Toast.makeText(mContext, "手机相机像素达不到要求！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    private IDataListener mDataListener = new IDataListener() {
        @Override
        public void onDetectorData(DetectorData detectorData) {
            mDetectorData = detectorData;
            ViseLog.i("识别数据:" + detectorData);
            mHandler.sendEmptyMessage(0);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_face_detector);
        mContext = this;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    protected void init() {
        mFace_detector_preview = (CameraPreview) findViewById(R.id.face_detector_preview);
        mFace_detector_face = (FaceRectView) findViewById(R.id.face_detector_face);
        mFace_detector_face.setZOrderOnTop(true);
        mFace_detector_face.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mFace_detector_take_photo = (Button) findViewById(R.id.face_detector_take_photo);
        mFace_detector_distance = (TextView) findViewById(R.id.face_detector_distance);

        // 点击SurfaceView，切换摄相头
        mFace_detector_preview.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View v) {
                // 只有一个摄相头，不支持切换
                if (Camera.getNumberOfCameras() == 1) {
                    return;
                }
                if (mDetectorProxy == null) {
                    return;
                }
                mDetectorProxy.closeCamera();
                if (Camera.CameraInfo.CAMERA_FACING_FRONT == mDetectorProxy.getCameraId()) {
                    mDetectorProxy.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
                } else {
                    mDetectorProxy.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
                }
                mDetectorProxy.openCamera();
            }
        });

        mFace_detector_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFace_detector_preview.getCamera().takePicture(new Camera.ShutterCallback() {
                    @Override
                    public void onShutter() {

                    }
                }, null, new Camera.PictureCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        mFace_detector_preview.getCamera().stopPreview();
                        saveImage(bytes);
                        finish();
                    }
                });
            }
        });

        mDetectorProxy = new DetectorProxy.Builder(mFace_detector_preview)
                .setMinCameraPixels(3000000)
                .setCheckListener(mCameraCheckListener)
                .setDataListener(mDataListener)
                .setFaceRectView(mFace_detector_face)
                .setDrawFaceRect(true)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDetectorProxy != null) {
            mDetectorProxy.detector();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDetectorProxy != null) {
            mDetectorProxy.release();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    /**
     * 保存拍照的图片
     *
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void saveImage(byte[] data) {
        File pictureFile = getOutputFile(mContext, "face", "photo.jpg");//拍照图片
        File avatarFile; avatarFile = getOutputFile(mContext, "face", "avatar.jpg");//截取人脸图片
        MainActivity.NEWFILE = getOutputFile(mContext, "face", "avatar.jpg");//截取人脸图片
        if (pictureFile == null || avatarFile == null) {
            return;
        }
        if (pictureFile.exists()) {
            pictureFile.delete();
        }
        if (avatarFile.exists()) {
            avatarFile.delete();
        }
        Rect rect = new Rect();
        if (mDetectorData != null && mDetectorData.getFaceRectList() != null
                && mDetectorData.getFaceRectList().length > 0
                && mDetectorData.getFaceRectList()[0].right > 0) {
            rect = mDetectorData.getFaceRectList()[0];
        }
        Toast.makeText( mContext, "save picture start!", Toast.LENGTH_SHORT ).show();
        Bitmap bitmap = getImage(data, rect, 1500, 2000, pictureFile, avatarFile);
        Toast.makeText( mContext, "save picture complete!", Toast.LENGTH_SHORT ).show();
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private String getDiskCacheDir(Context context, String dirName) {
        String cachePath = "";
        if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable())
                && context != null && context.getExternalCacheDir() != null) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            if (context != null && context.getCacheDir() != null) {
                cachePath = context.getCacheDir().getPath();
            }
        }
        return cachePath + File.separator + dirName;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private File getOutputFile(Context context, String dirName, String fileName) {
        File dirFile = new File(getDiskCacheDir(context, dirName));
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                ViseLog.d("failed to create directory");
                return null;
            }
        }
        File file = new File(dirFile.getPath() + File.separator + fileName);
        return file;
    }

    private Bitmap getImage(byte[] data, Rect rect, float ww, float hh, File pictureFile, File avatarFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        int w = options.outWidth;
        int h = options.outHeight;
        float scale = 1.0F;
        if (w < h) {
            scale = ww / (float) w;
            if (hh / (float) h > scale) {
                scale = hh / (float) h;
            }
        } else {
            scale = ww / (float) h;
            if (hh / (float) w > scale) {
                scale = hh / (float) w;
            }
        }

        Bitmap scaleBitmap = scaleImage(bitmap, (int) ((float) w * scale), (int) ((float) h * scale));
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        return compressImage(scaleBitmap, rect, 1536, pictureFile, avatarFile);
    }

    private Bitmap compressImage(Bitmap image, Rect rect, int size, File pictureFile, File avatarFile) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = image;
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;

        while (baos.toByteArray().length / 1024 > size) {
            baos.reset();
            options -= 5;
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(pictureFile);
            if (mFace_detector_preview != null) {
                if (mFace_detector_preview.getCameraId() == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    bitmap = rotaingImageView(360 - mFace_detector_preview.getDisplayOrientation(), bitmap);
                } else {
                    bitmap = rotaingImageView(mFace_detector_preview.getDisplayOrientation(), bitmap);
                }
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, fileOutputStream);
        } catch (FileNotFoundException var18) {
            ViseLog.e("File not found: " + var18.getMessage());
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException var16) {
                ViseLog.e("Error accessing file: " + var16.getMessage());
            }

        }

        float scale = (float) bitmap.getHeight() / ViewUtil.getScreenHeight(mContext);
        if (rect.right > 0) {
            int top = (int) (rect.top * scale);
            int bottom = (int) (rect.bottom * scale);
            rect.left = 0;
            rect.right = bitmap.getWidth();
            rect.top = top - (bitmap.getWidth() - (bottom - top)) / 2;
            rect.bottom = bottom + (bitmap.getWidth() - (bottom - top)) / 2;
        } else {
            rect.left = 0;
            rect.right = bitmap.getWidth();
            rect.top = (bitmap.getHeight() - bitmap.getWidth()) / 2;
            rect.bottom = (bitmap.getHeight() - bitmap.getWidth()) / 2 + bitmap.getWidth();
        }
        Bitmap avatarBitmap = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
        try {
            fileOutputStream = new FileOutputStream(avatarFile);
            avatarBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
        } catch (FileNotFoundException var18) {
            ViseLog.e("File not found: " + var18.getMessage());
        } finally {
            if (avatarBitmap != null && !avatarBitmap.isRecycled()) {
                avatarBitmap.recycle();
                avatarBitmap = null;
            }
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException var16) {
                ViseLog.e("Error accessing file: " + var16.getMessage());
            }

        }

        if (baos != null) {
            try {
                baos.close();
            } catch (IOException var17) {
                var17.printStackTrace();
            }
        }

        if (image != null && !image.isRecycled()) {
            image.recycle();
            image = null;
        }

        return bitmap;
    }

    private Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight) {
        if (bm == null) {
            return null;
        } else {
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = (float) newWidth / (float) width;
            float scaleHeight = (float) newHeight / (float) height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
            if (bm != null & !bm.isRecycled()) {
                bm.recycle();
                bm = null;
            }

            return newbm;
        }
    }

    private Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) angle);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap != bitmap && bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        return resizedBitmap;
    }

}