package com.example.android.sunshine.sync;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.example.android.sunshine.sync.SunshineSyncIntentService;

// COMP (9) Create a class called SunshineSyncUtils
public class SunshineSyncUtils {

    //  COMP (10) Create a public static void method called startImmediateSync
    public static void startImmediateSync(Context context) {
        //  COMP (11) Within that method, start the SunshineSyncIntentService
        Intent startSyncIntent = new Intent(context, SunshineSyncIntentService.class);
        context.startService(startSyncIntent);
    }
}
