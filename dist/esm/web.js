import { WebPlugin } from '@capacitor/core';
export class ScreenEventsWeb extends WebPlugin {
    async echo(options) {
        console.log('ECHO', options);
        return options;
    }
    async isScreenOn() {
        throw new Error('Method not implemented.');
    }
    async getUsageStats(options) {
        console.log('ECHO', options);
        //return options;
        throw new Error('Method not implemented.');
    }
    async getUsageEvents(options) {
        console.log('ECHO', options);
        //return options;
        throw new Error('Method not implemented.');
    }
}
//# sourceMappingURL=web.js.map