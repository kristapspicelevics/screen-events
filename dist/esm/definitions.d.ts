import { PluginListenerHandle } from "@capacitor/core";
export interface ScreenEventsPlugin {
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
    /**
    * Checks if screen is on
    *
    * @since 0.0.1
    */
    isScreenOn(): Promise<{
        result: boolean;
    }>;
    addListener(eventName: 'screenOn' | 'screenOff', listenerFunc: () => void): Promise<PluginListenerHandle> & PluginListenerHandle;
}
