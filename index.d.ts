import { PluginListenerHandle } from "@capacitor/core";

declare module 'screen-events' {
  // Define the types for the module here
  // Example:
  export function echo(param: string): void;
  export function isScreenOn(): boolean;
  export function addListener(eventName: 'screenOn' | 'screenOff', listenerFunc: () => void): Promise<PluginListenerHandle> & PluginListenerHandle;
}