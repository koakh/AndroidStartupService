package koakh.com.koakhandroidstartupservice.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import koakh.com.koakhandroidstartupservice.app.AppSingleton;
import koakh.com.koakhandroidstartupservice.util.NetworkUtil;

public class NetworkChangeReceiver extends BroadcastReceiver {

  private AppSingleton mApp;

  @Override
  public void onReceive(final Context context, final Intent intent) {

    String status = NetworkUtil.getConnectivityStatusString(context);
    Toast.makeText(context, status, Toast.LENGTH_SHORT).show();

    // Update Singleton
    mApp.setConnectivityStatus(NetworkUtil.getConnectivityStatus(context));

    // Update Ui
    mApp.getMainActivity().updateUiComponentNetworkConnectionStatus();
  }
}