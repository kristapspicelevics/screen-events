import Foundation
import Capacitor
import DeviceActivity
import BackgroundTasks

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

    private var backgroundDate: Date?
    private var totalForegroundTime: TimeInterval = 0

    override public func load() {
        NotificationCenter.default.addObserver(self, selector: #selector(screenStateDidChange(_:)), name: UIScreen.didConnectNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(screenStateDidChange(_:)), name: UIScreen.didDisconnectNotification, object: nil)
    }

    @objc private func appDidEnterBackground() {
        backgroundDate = Date()
    }

    @objc private func appWillEnterForeground() {
        if let backgroundDate = backgroundDate {
            let timeSpent = Date().timeIntervalSince(backgroundDate)
            totalForegroundTime += timeSpent
        }
    }

    @objc func getTotalScreenTime(_ call: CAPPluginCall) {
        call.resolve([
            "totalScreenTime": totalForegroundTime
        ])
    }

    @objc func resetScreenTime(_ call: CAPPluginCall) {
        totalForegroundTime = 0
        saveScreenTime()
        call.resolve()
    }

    private func saveScreenTime() {
        UserDefaults.standard.set(totalForegroundTime, forKey: "totalForegroundTime")
    }

    private func loadScreenTime() {
        totalForegroundTime = UserDefaults.standard.double(forKey: "totalForegroundTime")
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

    private var backgroundTask: UIBackgroundTaskIdentifier = .invalid

    @objc func start(_ call: CAPPluginCall) {
        if #available(iOS 13.0, *) {
            BGTaskScheduler.shared.register(forTaskWithIdentifier: "com.yourcompany.backgroundkeepalive", using: nil) { task in
                self.handleAppRefresh(task: task as! BGAppRefreshTask)
            }
            scheduleAppRefresh()
        } else {
            // For iOS 12 and below
            self.startLegacyBackgroundTask()
        }
        call.resolve()
    }

    @objc func stop(_ call: CAPPluginCall) {
        if #available(iOS 13.0, *) {
            BGTaskScheduler.shared.cancelAllTaskRequests()
        } else {
            self.endLegacyBackgroundTask()
        }
        call.resolve()
    }

    @available(iOS 13.0, *)
    func scheduleAppRefresh() {
        let request = BGAppRefreshTaskRequest(identifier: "com.yourcompany.backgroundkeepalive")
        request.earliestBeginDate = Date(timeIntervalSinceNow: 15 * 60) // 15 minutes from now
        do {
            try BGTaskScheduler.shared.submit(request)
        } catch {
            print("Could not schedule app refresh: \(error)")
        }
    }

    @available(iOS 13.0, *)
    func handleAppRefresh(task: BGAppRefreshTask) {
        scheduleAppRefresh()
        task.setTaskCompleted(success: true)
    }

    func startLegacyBackgroundTask() {
        backgroundTask = UIApplication.shared.beginBackgroundTask {
            // Clean up code if the background task is terminated
            UIApplication.shared.endBackgroundTask(self.backgroundTask)
            self.backgroundTask = .invalid
        }
    }

    func endLegacyBackgroundTask() {
        if backgroundTask != .invalid {
            UIApplication.shared.endBackgroundTask(backgroundTask)
            backgroundTask = .invalid
        }
    }

        private var isObserving = false
    
    
    @objc func screenDidConnect(notification: NSNotification) {
        notifyListeners("screenOn", data: ["status": "on"])
    }
    
    @objc func screenDidDisconnect(notification: NSNotification) {
        notifyListeners("screenOff", data: ["status": "off"])
    }

    deinit {
        NotificationCenter.default.removeObserver(self, name: UIScreen.didConnectNotification, object: nil)
        NotificationCenter.default.removeObserver(self, name: UIScreen.didDisconnectNotification, object: nil)
    }
}
