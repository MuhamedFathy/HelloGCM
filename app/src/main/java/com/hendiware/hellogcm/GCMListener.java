package com.hendiware.hellogcm;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;


public class GCMListener extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        Log.e("Notification Received!:", data.getString("message"));
    }
}
