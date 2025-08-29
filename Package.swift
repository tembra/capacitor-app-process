// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorCommunityAppProcess",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorCommunityAppProcess",
            targets: ["AppProcessPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "AppProcessPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/AppProcessPlugin"),
        .testTarget(
            name: "AppProcessPluginTests",
            dependencies: ["AppProcessPlugin"],
            path: "ios/Tests/AppProcessPluginTests")
    ]
)