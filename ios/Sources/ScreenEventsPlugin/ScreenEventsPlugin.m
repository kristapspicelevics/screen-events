#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>


// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(ScreenEventsPlugin, "ScreenEvents",
    CAP_PLUGIN_METHOD( echo, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD( getTotalScreenTime, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD( resetScreenTime, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD( start, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD( stop, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD( isScreenOn, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD( screenDidConnect, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD( screenDidDisconnect, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD( removeAllListeners, CAPPluginReturnPromise);
)