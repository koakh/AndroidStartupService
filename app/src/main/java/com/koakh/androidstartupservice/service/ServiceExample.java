  package com.koakh.androidstartupservice.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

import com.koakh.androidstartupservice.app.AppSingleton;
import com.koakh.androidstartupservice.ui.MainActivity;
import com.koakh.androidstartupservice.R;

  public class ServiceExample extends Service {

  // Constants
  public final static String LOCAL_SERVICE_MESSAGE = "serviceMessage";

  // Application
  private AppSingleton mApp;
  private Context mContext;
  // Binder given to clients
  private final IBinder mBinder = new LocalBinder();
  // Random number generator
  private final Random mGenerator = new Random();
  //Notification
  private NotificationManager mNotificationManager;
  private Notification.Builder mNotificationBuilder;
  //Runnable
  private LenghtyOperationRunnable mLenghtyOperationRunnable;
  private Thread mLenghtyOperationThread;

  @Override
  public void onCreate() {
    super.onCreate();

    //Get Singleton
    mApp = AppSingleton.getInstance();
    mContext = mApp.getContext();

    String message = "Service Created";
    Log.d(mApp.TAG, message);
    if (AppSingleton.SHOW_TOASTS) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    String message = "Service Destroyed";
    Log.d(mApp.TAG, message);
    if (AppSingleton.SHOW_TOASTS) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    //Stop
    if (mLenghtyOperationThread != null && mLenghtyOperationThread.isAlive()) {
      mLenghtyOperationRunnable.setStop(true);
      //Call Interrupt to stop Thread
      mLenghtyOperationThread.interrupt();
      mLenghtyOperationThread = null;
    }

    //Disable Singleton IsServiceRunning
    mApp.setServiceRunning(false);
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();

    String message = "Service Low Memory";
    Log.d(mApp.TAG, message);
    if (AppSingleton.SHOW_TOASTS) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {

    String message = "Service Start Command";
    Log.d(mApp.TAG, message);
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    //Enable Singleton IsServiceRunning
    mApp.setServiceRunning(true);

    Bundle extras = intent.getExtras();
    String imageURL = extras.getString("ImageFileURL");
    String fileName = extras.getString("FileName");
    String[] fileList = extras.getStringArray("FileList");

    Log.d(mApp.TAG, String.format("Bundle: %s, %s", imageURL, fileName));
    for (int i = 0; i < fileList.length; i++) {
      Log.d(mApp.TAG, String.format("file: %s", fileList[i]));
    }

    //Send Initial Broadcast Message to Update Activity UI
    //sendBroadcastMessage();

    //Initialize NotificationBuilder
    mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    mNotificationBuilder = new Notification.Builder(mContext);
    //Assign local members to Application Singleton
    mApp.setNotificationManager(mNotificationManager);
    mApp.setNotificationBuilder(mNotificationBuilder);

    //Prepare PendingIntent to Call MainActivity, used to call activity when press notification
    Intent notificationIntent = new Intent(mContext, MainActivity.class);
    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, (int) System.currentTimeMillis(), notificationIntent, 0);

    //Initialize Notification
    mNotificationBuilder
      .setPriority(Notification.PRIORITY_HIGH)
      .setContentTitle(mContext.getString(R.string.app_name))
      .setContentText(mContext.getString(R.string.global_service_running))
      .setSmallIcon(R.drawable.ic_download)
      .setContentIntent(pendingIntent)
    ;

    //Build Notification Object to Assign to OnBind
    Notification notification = mNotificationBuilder.build();

    //Start Service in ForeGround with OnGoing Notification
    startForeground(mApp.NOTIFICATION_UNIQUE_ID, notification);

    //Launch a lengthy operation
    testProgressNotification();

    // We want this service to continue running until it is explicitly
    // stopped, so return sticky.
    return START_STICKY;

    //replaced with > return startForeground and START_STICKY;
    //return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public IBinder onBind(Intent arg0) {

    String message = "Service onBind";
    Log.d(mApp.TAG, message);
    if (AppSingleton.SHOW_TOASTS) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    return mBinder;
  }

  @Override
  public boolean onUnbind(Intent intent) {

    String message = "Service onUnbind";
    Log.d(mApp.TAG, message);
    if (AppSingleton.SHOW_TOASTS) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    return super.onUnbind(intent);
  }

  @Override
  public void onRebind(Intent intent) {

    String message = "Service onRebind";
    Log.d(mApp.TAG, message);
    if (AppSingleton.SHOW_TOASTS) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    super.onRebind(intent);
  }

  //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

  /**
   * Class used for the client Binder.  Because we know this service always
   * runs in the same process as its clients, we don't need to deal with IPC.
   * <p/>
   * The LocalBinder provides the getService() method for clients to retrieve the current instance
   * of LocalService. This allows clients to call public methods in the service.
   * For example, clients can call getRandomNumber() from the service.
   */
  public class LocalBinder extends Binder {
    public ServiceExample getService() {
      // Return this instance of LocalService so clients can call public methods
      return ServiceExample.this;
    }
  }

  /**
   * Test ProgressBar Notification
   * Displaying Progress in a Notification
   * http://developer.android.com/training/notify-user/display-progress.html
   * http://javatechig.com/android/progress-notification-in-android-example
   */
  public void testProgressNotification() {

    //Reuse and Change base Notification
    mNotificationBuilder.setContentTitle("Service Sample : Test Long Progress")
      .setContentText("test in progress...")
      .setSmallIcon(R.drawable.ic_download);

    mLenghtyOperationRunnable = new LenghtyOperationRunnable(this);
    mLenghtyOperationThread = new Thread(mLenghtyOperationRunnable);
    mLenghtyOperationThread.start();

    // Start a lengthy operation in a background thread
/*
    new Thread(
      new Runnable() {
        @Override
        public void run() {
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
            sendBroadcastMessage(incr);

            // Sleeps the thread, simulating an operation
            // that takes time
            try {
              // Sleep for 1 seconds
              Thread.sleep(1 * 2000);
            } catch (InterruptedException e) {
              Log.d(mApp.TAG, "Sleep failure");
            }
          }
          // When the loop is finished, updates the notification
          mNotificationBuilder.setContentText("Operation complete")
            // Removes the progress bar
            .setProgress(0, 0, false);
          mNotificationManager.notify(mApp.NOTIFICATION_UNIQUE_ID, mNotificationBuilder.build());
        }
      }
      // Starts the thread by calling the run() method in its Runnable
    ).start();
*/

  }

  //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
  //Client Methods/ Called with bound Components

  public void sendBroadcastMessage(int progress) {

    //Test Send Broadcast to update Activity UI
    Intent broadcastIntent = new Intent(LOCAL_SERVICE_MESSAGE);
    broadcastIntent.putExtra("progress", progress);
    broadcastIntent.putExtra("currentSpeed", getRandomNumber());
    broadcastIntent.putExtra("latitude", getRandomNumber());
    broadcastIntent.putExtra("longitude", getRandomNumber());
    LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
  }

  //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
  //Client Methods/ Called with bound Components

  public int getRandomNumber() {
    return mGenerator.nextInt(100);
  }
}
