import Foundation
import Capacitor
import DeviceActivity
import BackgroundTasks

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(ScreenEventsPlugin)
public class ScreenEventsPlugin: CAPPlugin {

    private let implementation = ScreenEvents()

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": implementation.echo(value)
        ])
    }
}
