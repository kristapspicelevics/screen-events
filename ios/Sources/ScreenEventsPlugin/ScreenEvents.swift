import Foundation

@objc public class ScreenEvents: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }

    @objc public func getTotalScreenTime(_ value: String) -> String {
        print(value)
        return value
    }

    @objc public func resetScreenTime(_ value: String) -> String {
        print(value)
        return value
    }

    @objc public func start(_ value: String) -> String {
        print(value)
        return value
    }

    @objc public func stop(_ value: String) -> String {
        print(value)
        return value
    }

    @objc public func isScreenOn(_ value: String) -> String {
        print(value)
        return value
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
