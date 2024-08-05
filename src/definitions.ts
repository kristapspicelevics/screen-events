import { PluginListenerHandle } from "@capacitor/core";

export interface ScreenEventsPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;

  /**
  * Checks if screen is on
  *
  * @since 0.0.1
  */
  isScreenOn(): Promise<{ result: boolean }>;

  getUsageStats(options: {startTime: number, endTime: number}): Promise<{ result: string; }>;

  getUsageEvents(options: {startTime: number, endTime: number}): Promise<{ result: string; }>;
  
  addListener(eventName: 'screenOn' | 'screenOff', listenerFunc: () => void): Promise<PluginListenerHandle> & PluginListenerHandle;

  checkPermissions(): Promise<{ result: boolean }>;

  start(): Promise<{ result: boolean }>;

  stop(): Promise<{ result: boolean }>;

  getTotalScreenTime(): Promise<{ result: string }>

  resetScreenTime(): Promise<{ result: string  }>

    /**
  * Checks permission once
  *
  * @since 0.0.2
  */

  resetPermissionRequest(): Promise<{ result: boolean }>;
}
