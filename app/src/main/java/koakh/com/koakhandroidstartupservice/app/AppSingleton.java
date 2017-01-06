package koakh.com.koakhandroidstartupservice.app;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import koakh.com.koakhandroidstartupservice.ui.MainActivity;

public class AppSingleton extends Application {

  //Constants
  public final static String TAG = "KoakhStartupService";
  public final static boolean SHOW_TOASTS = false;
  public final static int NOTIFICATION_UNIQUE_ID = 2800;

  //Singleton
  private static AppSingleton mAppInstance = new AppSingleton();
  private Context mContext;
  private MainActivity mMainActivity;
  //Service
  private boolean mIsServiceRunning = false;
  private boolean mIsServiceTaskLengthyOperationRunning = false;
  //Notification
  private NotificationManager mNotificationManager;
  private Notification.Builder mNotificationBuilder;
  //Network
  private int mConnectivityStatus;

  @Override
  public void onCreate() {
    super.onCreate();
  }

  public static AppSingleton getInstance() {
    return mAppInstance;
  }

  public Context getContext() {
    return mContext;
  }

  public void setContext(Context context) {
    mContext = context;
  }

  public MainActivity getMainActivity() {
    return mMainActivity;
  }

  public void setMainActivity(MainActivity pAppContext) {
    mMainActivity = pAppContext;
  }

  public boolean isServiceRunning() {
    return mIsServiceRunning;
  }

  public void setServiceRunning(boolean serviceRunning) {
    mIsServiceRunning = serviceRunning;
  }

  public boolean isServiceTaskLengthyOperationRunning() {
    return mIsServiceTaskLengthyOperationRunning;
  }

  public void setServiceTaskLengthyOperationRunning(boolean serviceTaskLengthyOperationRunning) {
    mIsServiceTaskLengthyOperationRunning = serviceTaskLengthyOperationRunning;
  }

  public NotificationManager getNotificationManager() {
    return mNotificationManager;
  }

  public void setNotificationManager(NotificationManager notificationManager) {
    mNotificationManager = notificationManager;
  }

  public Notification.Builder getNotificationBuilder() {
    return mNotificationBuilder;
  }

  public void setNotificationBuilder(Notification.Builder notificationBuilder) {
    mNotificationBuilder = notificationBuilder;
  }

  public int getConnectivityStatus() {
    return mConnectivityStatus;
  }

  public void setConnectivityStatus(int connectivityStatus) {
    mConnectivityStatus = connectivityStatus;
  }
}
