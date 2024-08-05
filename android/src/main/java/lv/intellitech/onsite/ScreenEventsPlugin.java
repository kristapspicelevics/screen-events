package lv.intellitech.onsite;

import android.app.usage.UsageStats;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.os.PowerManager;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.AppOpsManager;
import android.os.Build;

import java.util.List;
import android.util.Log;
import android.content.SharedPreferences;

@CapacitorPlugin(name = "ScreenEvents")
public class ScreenEventsPlugin extends Plugin {

    private ScreenEvents implementation = new ScreenEvents();

    private static final String PREFS_NAME = "usage_stats_prefs";
    private static final String PREF_KEY_PERMISSION_REQUESTED = "permission_requested";


    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    private BroadcastReceiver screenStateReceiver;

    @Override
    protected void handleOnStart() {
        super.handleOnStart();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        screenStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    notifyListeners("screenOn", new JSObject());
                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    notifyListeners("screenOff", new JSObject());
                }
            }
        };

        getContext().registerReceiver(screenStateReceiver, filter);
    }

    @Override
    protected void handleOnStop() {
        super.handleOnStop();
        
        if (screenStateReceiver != null) {
            getContext().unregisterReceiver(screenStateReceiver);
            screenStateReceiver = null;
        }
    }

    @PluginMethod
    public void isScreenOn(PluginCall call) {
        PowerManager powerManager = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = powerManager.isInteractive();
        JSObject result = new JSObject();
        result.put("screenOn", isScreenOn);
        call.resolve(result);
    }

    @PluginMethod
    public void getUsageStats(PluginCall call) {
        if (!hasUsageStatsPermission()) {
            call.reject("Permission required");
            openUsageAccessSettings();
            return;
        }

        Long startTime = call.getLong("startTime");
        Long endTime = call.getLong("endTime");

        if (startTime == null || endTime == null) {
            call.reject("Must provide both startTime and endTime");
            return;
        }

        UsageStatsManager usageStatsManager = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);

        // long endTime = System.currentTimeMillis();
        // long startTime = endTime - 24 * 60 * 60 * 1000; // Last 24 hours

        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime, endTime);

        JSArray results = new JSArray();

        if (usageStatsList != null) {
            for (UsageStats stats : usageStatsList) {
                if (stats.getTotalTimeInForeground() > 0) {
                    JSObject stat = new JSObject();
                    stat.put("packageName", stats.getPackageName());
                    stat.put("totalTimeInForeground", stats.getTotalTimeInForeground());
                    stat.put("firstTimeStamp", stats.getFirstTimeStamp());
                    stat.put("lastTimeStamp", stats.getLastTimeStamp());
                    stat.put("lastTimeUsed", stats.getLastTimeUsed());
                    stat.put("totalTimeVisible", stats.getTotalTimeVisible());
                    stat.put("lastTimeVisible", stats.getLastTimeVisible());
                    stat.put("totalTimeForegroundServiceUsed", stats.getTotalTimeForegroundServiceUsed());
                    results.put(stat);
                }
            }
        }

        JSObject ret = new JSObject();
        ret.put("usageStats", results);
        call.resolve(ret);
    }

    @PluginMethod
    public void getUsageEvents(PluginCall call) {

        Long startTime = call.getLong("startTime");
        Long endTime = call.getLong("endTime");
        //String packageName = call.getString("packageName");

        if (startTime == null || endTime == null) {
            call.reject("Must provide startTime, endTime");
            return;
        }

        UsageStatsManager usageStatsManager = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);

        UsageEvents usageEvents = usageStatsManager.queryEvents(startTime, endTime);
        UsageEvents.Event event = new UsageEvents.Event();

        JSArray results = new JSArray();
        Long lastEventTime = null;
        Long totalForegroundTime = 0L;

        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                lastEventTime = event.getTimeStamp();
            } else if (event.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND && lastEventTime != null) {
                JSObject stat = new JSObject();
                stat.put("packageName", event.getPackageName());
                stat.put("startTime", lastEventTime);
                stat.put("endTime", event.getTimeStamp());
                stat.put("duration", event.getTimeStamp() - lastEventTime);
                results.put(stat);
                totalForegroundTime += event.getTimeStamp() - lastEventTime;
                lastEventTime = null;
            }
        }

        JSObject ret = new JSObject();
        ret.put("usageEvents", results);
        ret.put("totalForegroundTime", totalForegroundTime); // in milliseconds
        call.resolve(ret);
    }

    @PluginMethod
    public void checkPermissions(PluginCall call) {
        if (!hasUsageStatsPermission()) {
            call.reject("Permission required");
            openUsageAccessSettings();
            return;
        }
    }

    private boolean hasUsageStatsPermission() {
        // UsageStatsManager usageStatsManager = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
        // long endTime = System.currentTimeMillis();
        // long startTime = endTime - 1000 * 60;
        // List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        // return (stats != null && !stats.isEmpty());
        // int packagePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.PACKAGE_USAGE_STATS);
        // return packagePermission == PackageManager.PERMISSION_GRANTED;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppOpsManager appOps = (AppOpsManager) getContext().getSystemService(Context.APP_OPS_SERVICE);
            if (appOps != null) {
                int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        android.os.Process.myUid(), getContext().getPackageName());
                if (mode == AppOpsManager.MODE_DEFAULT) {
                    return getContext().checkCallingOrSelfPermission("android.permission.PACKAGE_USAGE_STATS") == PackageManager.PERMISSION_GRANTED;
                } else {
                    return mode == AppOpsManager.MODE_ALLOWED;
                }
            } else {
                Log.e("UsageStats", "AppOpsManager is null");
                return false;
            }
        } else {
            Log.e("UsageStats", "SDK version is below Lollipop");
            return false;
        }
    }

    private void openUsageAccessSettings() {
        SharedPreferences preferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean permissionRequested = preferences.getBoolean(PREF_KEY_PERMISSION_REQUESTED, false);
        if (!permissionRequested) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PREF_KEY_PERMISSION_REQUESTED, true);
            editor.apply();
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    }
}
