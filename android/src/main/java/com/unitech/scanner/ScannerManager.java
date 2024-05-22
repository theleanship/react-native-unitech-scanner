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
      if (Build.VERSION.SDK_INT >= 34 && reactContext.getApplicationInfo().targetSdkVersion >= 34) {
        reactContext.registerReceiver(barcodeBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
      } else {
        reactContext.registerReceiver(barcodeBroadcastReceiver, intentFilter);
      }
    }

    @Override
    public void onHostPause() {
      Log.d(TAG, "onHostPause");
      reactContext.unregisterReceiver(barcodeBroadcastReceiver);
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
    if (Build.VERSION.SDK_INT >= 34 && reactContext.getApplicationInfo().targetSdkVersion >= 34) {
      reactContext.registerReceiver(this.barcodeBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    } else {
      reactContext.registerReceiver(this.barcodeBroadcastReceiver, intentFilter);
    }
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
}
