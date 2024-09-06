import Foundation
import Capacitor
import DeviceActivity
import FamilyControls
import BackgroundTasks

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(ScreenEventsPlugin)
public class ScreenEventsPlugin: CAPPlugin {

    var totalScreenTime: TimeInterval = 0
    var startTime: Date? = nil
    
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
        NotificationCenter.default.addObserver(self, selector: #selector(screenDidLock), name: UIApplication.protectedDataWillBecomeUnavailableNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(screenDidUnlock), name: UIApplication.protectedDataDidBecomeAvailableNotification, object: nil)
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
        totalScreenTime = 0
        saveScreenTime()
        startTime = nil
        call.resolve(["screenEventsTimeReset": true])
    }

    private func saveScreenTime() {
        UserDefaults.standard.set(totalForegroundTime, forKey: "totalForegroundTime")
    }

    private func loadScreenTime() {
        totalForegroundTime = UserDefaults.standard.double(forKey: "totalForegroundTime")
    }

    @objc private func screenStateDidChange(_ notification: Notification) {
        if notification.name == UIApplication.protectedDataDidBecomeAvailableNotification {
            isScreenOn = true
            notifyListeners("screenOn", data: ["screen":"on"])
        } else if notification.name == UIApplication.protectedDataWillBecomeUnavailableNotification {
            isScreenOn = false
            notifyListeners("screenOff", data: ["screen":"off"])
        }
    }

    @objc func isScreenOn(_ call: CAPPluginCall) {
        call.resolve([
            "screenOn": isScreenOn
        ])
    }

    private var backgroundTask: UIBackgroundTaskIdentifier = .invalid

    @objc func start(_ call: CAPPluginCall) {
        print("screenEventsCurrent: ", totalScreenTime)
        self.startTime = Date()
        //NotificationCenter.default.addObserver(self, selector: #selector(screenDidLock), name: UIScreen.didDisconnectNotification, object: nil)
        //NotificationCenter.default.addObserver(self, selector: #selector(screenDidUnlock), name: UIScreen.didConnectNotification, object: nil)
        //NotificationCenter.default.addObserver(self, selector: #selector(screenDidLock), name: UIApplication.didEnterBackgroundNotification, object: nil)
        //NotificationCenter.default.addObserver(self, selector: #selector(screenDidUnlock), name: UIApplication.willEnterForegroundNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(screenDidLock), name: UIApplication.protectedDataWillBecomeUnavailableNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(screenDidUnlock), name: UIApplication.protectedDataDidBecomeAvailableNotification, object: nil)
        call.resolve(["screenEventsStarted":startTime])
    }
    
    @objc func screenDidLock() {
        print("screenEventsDidLock")
        if let startTime = self.startTime {
            self.totalScreenTime += Date().timeIntervalSince(startTime)
        }
    }

    @objc func screenDidUnlock() {
        print("screenEventsDidUnlock")
        self.startTime = Date()
    }
    
    @objc func stop(_ call: CAPPluginCall) {
        if let startTime = self.startTime {
            self.totalScreenTime += Date().timeIntervalSince(startTime)
        }
        
        let stopTime = Date()
        
        NotificationCenter.default.removeObserver(self)
        call.resolve([
            "screenEventsTotalScreenTime": self.totalScreenTime,
            "screenEventsStopped":stopTime
        ])
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
        NotificationCenter.default.removeObserver(self, name: UIApplication.protectedDataDidBecomeAvailableNotification, object: nil)
        NotificationCenter.default.removeObserver(self, name: UIApplication.protectedDataWillBecomeUnavailableNotification, object: nil)
    }

}
