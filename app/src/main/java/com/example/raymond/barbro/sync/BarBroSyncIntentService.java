package com.example.raymond.barbro.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by alex on 1/5/17.
 */

public class BarBroSyncIntentService extends IntentService {
    public BarBroSyncIntentService(){
        super("BarBroSyncIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        BarBroSyncTask.syncBarBro(this);
    }
}
