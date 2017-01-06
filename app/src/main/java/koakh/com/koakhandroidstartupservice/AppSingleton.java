package koakh.com.koakhandroidstartupservice;

import android.app.Application;
import android.content.Context;

public class AppSingleton extends Application {

  //Constants
  public final static String TAG = "KoakhStartupService";
  public final static int NOTIFICATION_UNIQUE_ID = 2800;

  //Singleton
  private static AppSingleton mAppInstance = new AppSingleton();
  private Context mContext;
  private MainActivity mMainActivity;
  //Service
  private boolean mIsServiceRunning = false;
  private boolean mIsServiceTaskLengthyOperationRunning = false;

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
}
