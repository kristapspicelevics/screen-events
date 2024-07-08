import Foundation

@objc public class ScreenEvents: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }

    @objc public func getTotalScreenTime() -> String {
        return
    }

    @objc public func resetScreenTime() ->String {
        return
    }

    @objc public func start() -> Bool {
        return
    }

    @objc public func stop() -> Bool {
        return
    }

    @objc public func isScreenOn() -> Bool {
        return
    }

    @objc public func screenDidConnect(_ value: String) -> String {
        print(value)
        return value
    }

    @objc public func screenDidDisconnect(_ value: String) -> String {
        print(value)
        return value
    }
}
