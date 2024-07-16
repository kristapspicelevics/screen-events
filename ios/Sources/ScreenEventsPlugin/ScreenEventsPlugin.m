#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>


// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(PushNotificationsPlugin, "ScreenEvents",
        CAPPluginMethod(name: echo, returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: getTotalScreenTime, returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: resetScreenTime, returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: start, returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: stop, returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: isScreenOn, returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: screenDidConnect, returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: screenDidDisconnect, returnType: CAPPluginReturnPromise)
)