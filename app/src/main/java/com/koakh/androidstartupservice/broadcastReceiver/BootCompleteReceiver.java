package com.koakh.androidstartupservice.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.koakh.androidstartupservice.app.AppSingleton;
import com.koakh.androidstartupservice.service.ServiceExample;

public class BootCompleteReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {

    String action = intent.getAction();
    AppSingleton mApp = AppSingleton.getInstance();

    Toast.makeText(context, "Boot Complete. Start Service", Toast.LENGTH_SHORT).show();

    Intent mIntentServiceExample = new Intent(mApp.getContext(), ServiceExample.class);
    mApp.getContext().startService(mIntentServiceExample);
  }
}