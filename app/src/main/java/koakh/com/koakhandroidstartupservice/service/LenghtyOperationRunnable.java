package koakh.com.koakhandroidstartupservice.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.util.Log;

import koakh.com.koakhandroidstartupservice.app.AppSingleton;

/**
 * Created by mario.monteiro on 06/01/2017.
 * http://stackoverflow.com/questions/10630737/how-to-stop-a-thread-created-by-implementing-runnable-interface
 */

class LenghtyOperationRunnable implements Runnable{

  // Application
  private AppSingleton mApp;
  //Service
  ServiceExample mService;
  //Notification
  private NotificationManager mNotificationManager;
  private Notification.Builder mNotificationBuilder;

  private Boolean stop = false;

  public LenghtyOperationRunnable(ServiceExample service) {
    mService = service;
  }

  public void run(){

    //Get Singleton
    mApp = AppSingleton.getInstance();
    mNotificationManager = mApp.getNotificationManager();
    mNotificationBuilder = mApp.getNotificationBuilder();

    while(!stop){

      int incr;
      // Do the "lengthy" operation 20 times/progress steps
      for (incr = 0; incr <= 100; incr += 5) {
        // Sets the progress indicator to a max value, the
        // current completion percentage, and "determinate"
        // state
        mNotificationBuilder.setProgress(100, incr, false);
        // Displays the progress bar for the first time.
        mNotificationManager.notify(mApp.NOTIFICATION_UNIQUE_ID, mNotificationBuilder.build());
        //Send Initial Broadcast Message to Update Activity UI
        mService.sendBroadcastMessage(incr);

        // Sleeps the thread, simulating an operation
        // that takes time
        try {
          // Sleep for 1 seconds
          Thread.sleep(1 * 1000);
        } catch (InterruptedException e) {
          Log.d(mApp.TAG, "Sleep failure");
        }

        // If Thread Stop Return For Loop
        if (stop) return;
      }
      // When the loop is finished, updates the notification
      mNotificationBuilder.setContentText("Operation complete")
        // Removes the progress bar
        .setProgress(0, 0, false);
      mNotificationManager.notify(mApp.NOTIFICATION_UNIQUE_ID, mNotificationBuilder.build());
    }
  }

  public Boolean getStop() {
    return stop;
  }

  public void setStop(Boolean stop) {
    this.stop = stop;
  }
}