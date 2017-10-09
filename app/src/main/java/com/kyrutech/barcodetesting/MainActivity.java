package com.kyrutech.barcodetesting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements BarCodeTracker.Callback {

    CameraSource mCameraSource;
    BarcodeDetector mDetector;

    SurfaceView mCameraView;

    private static final int CAMERA_PERMISSION_CAMERA = 0x000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraView = (SurfaceView) findViewById(R.id.cameraView);

        mDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.UPC_A | Barcode.UPC_E).build();
        BarCodeTrackerFactory barcodeFactory = new BarCodeTrackerFactory(this);
        Detector.Processor<Barcode> barcodeMultiProcessor = new MultiProcessor.Builder<>(barcodeFactory).build();
        mDetector.setProcessor(barcodeMultiProcessor);



        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            mCameraSource = new CameraSource.Builder(this, mDetector)
                    .setAutoFocusEnabled(true)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .build();

        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CAMERA);
        }

        mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //      TODO: CONSIDER CALLING
                        //ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.


                        return;
                    }
                    mCameraSource.start(mCameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie
                            .getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }
        });


//        try {
//            mCameraSource.start(mCameraView.getHolder());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onFound(Barcode barcode) {
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(barcode.displayValue);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager
                        .PERMISSION_GRANTED) {

                    Intent startMain = new Intent(MainActivity
                            .this, MainActivity
                            .class);
                    startActivity(startMain);

                } else {
                    if (ContextCompat.checkSelfPermission(MainActivity
                                    .this,
                            Manifest
                                    .permission
                                    .CAMERA)
                            != PackageManager
                            .PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity
                                        .this,
                                Manifest
                                        .permission
                                        .CAMERA)) {

                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(MainActivity
                                            .this,
                                    new String[]{Manifest.permission
                                            .CAMERA},
                                    CAMERA_PERMISSION_CAMERA);

                            // CAMERA_PERMISSION_CAMERA is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }
                }
                return;
            }
        }
    }
}
