import { PluginListenerHandle } from "@capacitor/core";

declare module 'screen-events' {
  // Define the types for the module here
  // Example:
  export function echo(param: string): void;
  export function isScreenOn(): boolean;
  export function addListener(eventName: 'screenOn' | 'screenOff', listenerFunc: () => void): Promise<PluginListenerHandle> & PluginListenerHandle;
  export function checkPermissions(): Promise<{ result: boolean }>;
  export function requestPermissions(): Promise<{ result: boolean }>;
  export function start(): void;
  export function stop(): void;
  export function getTotalScreenTime(): Promise<{ result: string }>;
  export function resetScreenTime(): Promise<{ result: string }>;
}