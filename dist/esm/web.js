import { WebPlugin } from '@capacitor/core';
export class ScreenEventsWeb extends WebPlugin {
    async echo(options) {
        console.log('ECHO', options);
        return options;
    }
    async isScreenOn() {
        throw new Error('Method not implemented.');
    }
    async getUsageStats() {
        throw new Error('Method not implemented.');
    }
}
//# sourceMappingURL=web.js.map