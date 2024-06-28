// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "ScreenEvents",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "ScreenEvents",
            targets: ["ScreenEventsPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "ScreenEventsPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/ScreenEventsPlugin"),
        .testTarget(
            name: "ScreenEventsPluginTests",
            dependencies: ["ScreenEventsPlugin"],
            path: "ios/Tests/ScreenEventsPluginTests")
    ]
)