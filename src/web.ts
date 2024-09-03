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

  async getUsageStats(options: {startTime: number, endTime: number}): Promise<{ result: string; }> {
    console.log('ECHO', options);
    //return options;
    throw new Error('Method not implemented.');
  }

  async getUsageEvents(options: {startTime: number, endTime: number}): Promise<{ result: string; }> {
    console.log('ECHO', options);
    //return options;
    throw new Error('Method not implemented.');
  }

  async checkPermissions(): Promise<{ result: boolean; }> {
    throw new Error('Method not implemented.');
  }

  async requestPermissions(): Promise<{ result: boolean; }> {
    throw new Error('Method not implemented.');
  }

  async start(): Promise<{ result: boolean }> {
    throw new Error('Method not implemented.');
  }

  async stop(): Promise<{ result: boolean }> {
    throw new Error('Method not implemented.');
  }

  async getTotalScreenTime(): Promise<{ result: string }> {
    throw new Error('Method not implemented.');
  }

  async resetScreenTime(): Promise<{ result: string  }> {
    throw new Error('Method not implemented.');
  }

  async removeAllListeners(): Promise<void> {
    throw new Error('Method not implemented.');
  }

}
