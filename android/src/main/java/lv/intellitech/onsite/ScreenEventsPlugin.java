package lv.intellitech.onsite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

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
}
