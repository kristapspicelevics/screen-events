import { registerPlugin } from '@capacitor/core';

import type { ScreenEventsPlugin } from './definitions';

const ScreenEvents = registerPlugin<ScreenEventsPlugin>('ScreenEvents', {
  web: () => import('./web').then(m => new m.ScreenEventsWeb()),
});

export * from './definitions';
export { ScreenEvents };
