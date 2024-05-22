package com.unitech.scanner;

import android.os.Build;
import android.content.Context;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.util.Log;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;

public class ScannerManager {
  private ScanManager scanManager;
  private ReactApplicationContext reactContext;
  private static final String TAG = "UTScannerManager";
  private BarcodeBroadcastReceiver barcodeBroadcastReceiver;
  private IntentFilter intentFilter = new IntentFilter(ScanManager.ACTION_DECODE);

  private final LifecycleEventListener lifecycleEventListener = new LifecycleEventListener() {
    @Override
    public void onHostResume() {
      Log.d(TAG, "onHostResume");
      compatRegisterReceiver(reactContext, barcodeBroadcastReceiver, intentFilter, true);
    }

    @Override
    public void onHostPause() {
      Log.d(TAG, "onHostPause");
      compatRegisterReceiver(reactContext, barcodeBroadcastReceiver, intentFilter, true);
    }

    @Override
    public void onHostDestroy() {
      Log.d(TAG, "onHostDestroy");
      reactContext.unregisterReceiver(barcodeBroadcastReceiver);
    }
  };

  public ScannerManager(ReactApplicationContext reactContext) {
    this.reactContext = reactContext;
    this.scanManager = new ScanManager();
    this.barcodeBroadcastReceiver = new BarcodeBroadcastReceiver(reactContext);
    compatRegisterReceiver(reactContext, this.barcodeBroadcastReceiver, this.intentFilter, true);
    this.reactContext.addLifecycleEventListener(lifecycleEventListener);
  }

  public boolean getScannerState() {
    boolean result = scanManager.getScannerState();
    Log.d(TAG, "getScannerState: " + result);
    return result;
  }

  public boolean openScanner() {
    boolean result = scanManager.openScanner();
    Log.d(TAG, "openScanner: " + result);
    return result;
  }

  public boolean closeScanner() {
    boolean result = scanManager.closeScanner();
    Log.d(TAG, "closeScanner: " + result);
    return result;
  }

  public boolean startDecode() {
    boolean isScannerOn = this.getScannerState();
    if (!isScannerOn) {
      this.openScanner();
    }

    boolean result = scanManager.startDecode();
    Log.d(TAG, "startDecode: " + result);
    return result;
  }

  public boolean stopDecode() {
    boolean result = scanManager.stopDecode();
    Log.d(TAG, "stopDecode: " + result);
    return result;
  }

  public boolean getTriggerLockState() {
    boolean result = scanManager.getTriggerLockState();
    Log.d(TAG, "getTriggerLockState: " + result);
    return result;
  }

  public boolean lockTrigger() {
    boolean result = scanManager.lockTrigger();
    Log.d(TAG, "lockTrigger: " + result);
    return result;
  }

  public boolean unlockTrigger() {
    boolean result = scanManager.unlockTrigger();
    Log.d(TAG, "unlockTrigger: " + result);
    return result;
  }

  /**
   * Starting with Android 14, apps and services that target Android 14 and use context-registered
   * receivers are required to specify a flag to indicate whether or not the receiver should be
   * exported to all other apps on the device: either RECEIVER_EXPORTED or RECEIVER_NOT_EXPORTED
   *
   * <p>https://developer.android.com/about/versions/14/behavior-changes-14#runtime-receivers-exported
   */
  private void compatRegisterReceiver(
      Context context, BroadcastReceiver receiver, IntentFilter filter, boolean exported) {
    if (Build.VERSION.SDK_INT >= 34 && context.getApplicationInfo().targetSdkVersion >= 34) {
      context.registerReceiver(
          receiver, filter, exported ? Context.RECEIVER_EXPORTED : Context.RECEIVER_NOT_EXPORTED);
    } else {
      context.registerReceiver(receiver, filter);
    }
  }
}
