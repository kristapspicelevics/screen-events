export interface ScreenEventsPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  addListener(eventName: 'screenOn' | 'screenOff', listenerFunc: () => void): void;
}
