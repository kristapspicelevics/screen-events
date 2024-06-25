import { WebPlugin } from '@capacitor/core';
export class ScreenEventsWeb extends WebPlugin {
    async echo(options) {
        console.log('ECHO', options);
        return options;
    }
    isScreenOn() {
        throw new Error('Method not implemented.');
    }
}
//# sourceMappingURL=web.js.map