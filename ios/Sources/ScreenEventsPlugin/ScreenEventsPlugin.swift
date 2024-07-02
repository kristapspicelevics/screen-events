import Foundation
import Capacitor
import DeviceActivity

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(ScreenEventsPlugin)
public class ScreenEventsPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "ScreenEventsPlugin"
    public let jsName = "ScreenEvents"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "echo", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = ScreenEvents()

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": implementation.echo(value)
        ])
    }

    private var isScreenOn = true

    override public func load() {
        NotificationCenter.default.addObserver(self, selector: #selector(screenStateDidChange(_:)), name: UIScreen.didConnectNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(screenStateDidChange(_:)), name: UIScreen.didDisconnectNotification, object: nil)
    }

    @objc private func screenStateDidChange(_ notification: Notification) {
        if notification.name == UIScreen.didConnectNotification {
            isScreenOn = true
            notifyListeners("screenOn", data: [:])
        } else if notification.name == UIScreen.didDisconnectNotification {
            isScreenOn = false
            notifyListeners("screenOff", data: [:])
        }
    }

    @objc func isScreenOn(_ call: CAPPluginCall) {
        call.resolve([
            "screenOn": isScreenOn
        ])
    }
}
