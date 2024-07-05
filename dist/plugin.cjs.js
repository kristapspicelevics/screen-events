'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var core = require('@capacitor/core');

const ScreenEvents = core.registerPlugin('ScreenEvents', {
    web: () => Promise.resolve().then(function () { return web; }).then(m => new m.ScreenEventsWeb()),
});

class ScreenEventsWeb extends core.WebPlugin {
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

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    ScreenEventsWeb: ScreenEventsWeb
});

exports.ScreenEvents = ScreenEvents;
//# sourceMappingURL=plugin.cjs.js.map
