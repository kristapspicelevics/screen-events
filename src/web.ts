import { WebPlugin } from '@capacitor/core';

import type { ScreenEventsPlugin } from './definitions';

export class ScreenEventsWeb extends WebPlugin implements ScreenEventsPlugin {

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
  
  isScreenOn(): Promise<{ result: boolean; }> {
    throw new Error('Method not implemented.');
  }

}
