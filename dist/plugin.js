var capacitorScreenEvents = (function (exports, core) {
    'use strict';

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
        async getUsageStats() {
            throw new Error('Method not implemented.');
        }
    }

    var web = /*#__PURE__*/Object.freeze({
        __proto__: null,
        ScreenEventsWeb: ScreenEventsWeb
    });

    exports.ScreenEvents = ScreenEvents;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, capacitorExports);
//# sourceMappingURL=plugin.js.map
