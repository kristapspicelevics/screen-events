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

import java.util.List;

@CapacitorPlugin(name = "ScreenEvents")
public class ScreenEventsPlugin extends Plugin {

    private ScreenEvents implementation = new ScreenEvents();

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

        UsageStatsManager usageStatsManager = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);

        UsageEvents usageEvents = usageStatsManager.queryEvents(startTime, endTime);
        UsageEvents.Event event = new UsageEvents.Event();

        JSArray results = new JSArray();
        Long lastEventTime = null;
        Long totalForegroundTime = 0L;

        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getPackageName().equals(packageName)) {
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
        }

        JSObject ret = new JSObject();
        ret.put("usageEvents", results);
        ret.put("totalForegroundTime", totalForegroundTime); // in milliseconds
        call.resolve(ret);
    }

    private boolean hasUsageStatsPermission() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
        long endTime = System.currentTimeMillis();
        long startTime = endTime - 1000 * 60;
        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        return (stats != null && !stats.isEmpty());
    }

    private void openUsageAccessSettings() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

}
