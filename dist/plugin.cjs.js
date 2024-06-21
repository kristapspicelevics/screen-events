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
}

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    ScreenEventsWeb: ScreenEventsWeb
});

exports.ScreenEvents = ScreenEvents;
//# sourceMappingURL=plugin.cjs.js.map
