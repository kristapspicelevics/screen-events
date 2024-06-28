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

    @objc func getUsageEvents(_ call: CAPPluginCall) {
        let startDate = call.getString("startDate") ?? ""
        let endDate = call.getString("endDate") ?? ""

        // Assuming you have date strings in the correct format
        guard let start = ISO8601DateFormatter().date(from: startDate),
              let end = ISO8601DateFormatter().date(from: endDate) else {
            call.reject("Invalid date format")
            return
        }

        // Fetch DeviceActivity data
        let activityQuery = DeviceActivityDailyReportQuery(from: start, to: end)
        
        let store = DeviceActivityCenter.current
        
        store.query(activityQuery) { result in
            switch result {
            case .success(let report):
                var usageData = [[String: Any]]()
                
                for (category, data) in report {
                    var categoryData = [String: Any]()
                    categoryData["category"] = category.rawValue
                    categoryData["totalTime"] = data.totalTime // Assuming you have total time in seconds
                    usageData.append(categoryData)
                }
                
                call.resolve([
                    "usageData": usageData
                ])
                
            case .failure(let error):
                call.reject("Failed to retrieve data: \(error.localizedDescription)")
            }
        }
    }
}
