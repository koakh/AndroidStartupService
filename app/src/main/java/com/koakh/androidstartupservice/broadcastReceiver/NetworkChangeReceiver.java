package com.koakh.androidstartupservice.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.koakh.androidstartupservice.app.AppSingleton;
import com.koakh.androidstartupservice.util.NetworkUtil;

public class NetworkChangeReceiver extends BroadcastReceiver {

  private AppSingleton mApp;

  @Override
  public void onReceive(final Context context, final Intent intent) {

    // Get Application Singleton
    mApp = AppSingleton.getInstance();

    String status = NetworkUtil.getConnectivityStatusString(context);
    if (AppSingleton.SHOW_TOASTS) Toast.makeText(context, status, Toast.LENGTH_SHORT).show();

    // Update Singleton
    mApp.setConnectivityStatus(NetworkUtil.getConnectivityStatus(context));

    // Update Ui
    mApp.getMainActivity().updateUiComponentNetworkConnectionStatus();
  }
}