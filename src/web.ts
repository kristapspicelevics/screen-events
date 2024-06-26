import { WebPlugin } from '@capacitor/core';

import type { ScreenEventsPlugin } from './definitions';

export class ScreenEventsWeb extends WebPlugin implements ScreenEventsPlugin {

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
  
  async isScreenOn(): Promise<{ result: boolean; }> {
    throw new Error('Method not implemented.');
  }

  async getUsageStats(): Promise<{ result: string; }> {
    throw new Error('Method not implemented.');
  }

}
