package lv.intellitech.onsite;

import android.util.Log;

public class ScreenEvents {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }

    public Boolean isScreenOn(Boolean value) {
        Log.i("Echo", String.valueOf(value));
        return value;
    }

}
