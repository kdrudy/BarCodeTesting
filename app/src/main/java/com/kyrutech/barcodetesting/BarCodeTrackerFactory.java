package com.kyrutech.barcodetesting;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by kylerudy on 10/9/17.
 */

public class BarCodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private BarCodeTracker.Callback mCallBack;

    public BarCodeTrackerFactory(BarCodeTracker.Callback mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        return new BarCodeTracker(mCallBack);
    }
}
