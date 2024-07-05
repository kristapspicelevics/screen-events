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
    async checkPermissions() {
        throw new Error('Method not implemented.');
    }
    async start() {
        throw new Error('Method not implemented.');
    }
    async stop() {
        throw new Error('Method not implemented.');
    }
    async getTotalScreenTime() {
        throw new Error('Method not implemented.');
    }
    async resetScreenTime() {
        throw new Error('Method not implemented.');
    }
}
//# sourceMappingURL=web.js.map