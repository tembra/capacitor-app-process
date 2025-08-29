package com.getcapacitor.community.appprocess;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Debug.MemoryInfo;

import com.getcapacitor.Logger;

public class AppProcess {

  private static final String PREFS = "CapacitorAppProcessPrefs";
  private static final String KEY_LAST_RELAUNCH_TIMESTAMP = "lastRelaunchTimestamp";

  private static final long DEFAULT_COOLDOWN_MS = 60_000L;
  private static final long DEFAULT_WAIT_TIME_MS = 500L;

  private static final int RELAUNCH_REQUEST_CODE = 1001;

  private final Context context;
  private final Activity activity;

  public AppProcess(Context context, Activity activity) {
    this.context = context.getApplicationContext();
    this.activity = activity;
  }

  public int getPid() {
    return android.os.Process.myPid();
  }

  public float getPssMiB() throws Exception {
    ActivityManager activityManager = (ActivityManager) this.context
        .getSystemService(Context.ACTIVITY_SERVICE);

    if (activityManager == null) {
      throw new IllegalStateException("ActivityManager unavailable");
    }

    int pid = android.os.Process.myPid();

    MemoryInfo[] infos = activityManager.getProcessMemoryInfo(new int[] {pid});

    if (infos == null || infos.length == 0) {
      throw new IllegalStateException("MemoryInfo empty");
    }

    int pssKiB = infos[0].getTotalPss();

    return pssKiB / 1024f;
  }

  public void scheduleRelaunch(Long cooldownMs, Long waitTimeMs) throws Exception {
    long cooldown = (cooldownMs != null) ? cooldownMs : DEFAULT_COOLDOWN_MS;
    long waitTime = (waitTimeMs != null) ? waitTimeMs : DEFAULT_WAIT_TIME_MS;

    SharedPreferences prefs = this.context
        .getSharedPreferences(PREFS, Context.MODE_PRIVATE);

    long now = System.currentTimeMillis();
    long last = prefs.getLong(KEY_LAST_RELAUNCH_TIMESTAMP, 0L);

    if (now - last < cooldown) {
      throw new IllegalStateException("Relaunch on cooldown");
    }

    boolean persistPrefs = prefs.edit()
        .putLong(KEY_LAST_RELAUNCH_TIMESTAMP, now)
        .commit();

    if (!persistPrefs) {
      throw new IllegalStateException("Failed to persist relaunch timestamp");
    }

    Intent launch = this.context
        .getPackageManager()
        .getLaunchIntentForPackage(
            this.context
                .getPackageName());

    if (launch == null) {
      throw new IllegalStateException("Launch intent not found");
    }

    launch.addFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK
            | Intent.FLAG_ACTIVITY_CLEAR_TOP
            | Intent.FLAG_ACTIVITY_NO_ANIMATION);

    int flags = PendingIntent.FLAG_CANCEL_CURRENT;

    if (Build.VERSION.SDK_INT >= 23) {
      flags |= PendingIntent.FLAG_IMMUTABLE;
    }

    PendingIntent pendingIntent =
        PendingIntent.getActivity(this.context, RELAUNCH_REQUEST_CODE, launch, flags);

    AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);

    if (alarmManager == null) {
      throw new IllegalStateException("AlarmManager unavailable");
    }

    long when = System.currentTimeMillis() + waitTime;

    if (Build.VERSION.SDK_INT >= 23) {
      alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, when, pendingIntent);

      return;

    } else if (Build.VERSION.SDK_INT >= 19) {
      alarmManager.setExact(AlarmManager.RTC_WAKEUP, when, pendingIntent);

      return;
    }

    alarmManager.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
  }

  public void softKill() {
    try {
      activity.finishAffinity();
    } catch (Throwable throwable) {
      Logger.error("AppProcess", "softKill/finishAffinity() error", throwable);
    }

    try {
      activity.finish();
    } catch (Throwable throwable) {
      Logger.error("AppProcess", "softKill/finish() error", throwable);
    }

    android.os.Process.killProcess(android.os.Process.myPid());

    System.exit(0);
  }
}
