import Foundation

@objc public class ScreenEvents: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
