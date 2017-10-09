package com.kyrutech.barcodetesting;

import android.support.annotation.UiThread;

import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by kylerudy on 10/9/17.
 */

public class BarCodeTracker extends Tracker<Barcode> {

    private Callback mCallBack;

    public BarCodeTracker(Callback mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public void onNewItem(int i, Barcode barcode) {
        mCallBack.onFound(barcode);
    }

    public interface Callback {
        @UiThread
        void onFound(Barcode barcode);
    }
}
