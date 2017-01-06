package koakh.com.koakhandroidstartupservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mario.monteiro on 05/01/2017.
 */

public class MessageReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {

    //Get Application Singleton
    AppSingleton mApp = AppSingleton.getInstance();

    // Extract data included in the Intent
    String action = intent.getAction();
    Double currentSpeed = intent.getDoubleExtra("currentSpeed", 20);
    Double currentLatitude = intent.getDoubleExtra("latitude", 0);
    Double currentLongitude = intent.getDoubleExtra("longitude", 0);

    String message = String.format("action : %s : %s : %s :%s",
      action,
      currentSpeed.toString(),
      currentLatitude.toString(),
      currentLongitude.toString()
    );

    Log.d(mApp.TAG, message);
  }
}
