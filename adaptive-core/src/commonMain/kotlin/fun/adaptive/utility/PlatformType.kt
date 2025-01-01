package `fun`.adaptive.utility

enum class PlatformType {
    JVM,
    Android,
    iOS,
    JsBrowser,
    JsNode
}

expect val platformType: PlatformType