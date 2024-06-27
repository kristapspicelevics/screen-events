package lv.intellitech.onsite;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

        long endTime = System.currentTimeMillis();
        long startTime = endTime - 24 * 60 * 60 * 1000; // Last 24 hours

        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        JSArray results = new JSArray();

        if (usageStatsList != null) {
            for (UsageStats stats : usageStatsList) {
                if (stats.getTotalTimeInForeground() > 0) {
                    JSObject stat = new JSObject();
                    stat.put("packageName", stats.getPackageName());
                    stat.put("totalTimeInForeground", stats.getTotalTimeInForeground());
                    results.put(stat);
                }
            }
        }

        JSObject ret = new JSObject();
        ret.put("usageStats", results);
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
