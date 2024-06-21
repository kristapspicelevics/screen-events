import { registerPlugin } from '@capacitor/core';
const ScreenEvents = registerPlugin('ScreenEvents', {
    web: () => import('./web').then(m => new m.ScreenEventsWeb()),
});
export * from './definitions';
export { ScreenEvents };
//# sourceMappingURL=index.js.map