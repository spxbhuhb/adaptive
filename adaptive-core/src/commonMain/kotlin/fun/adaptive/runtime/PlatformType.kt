package `fun`.adaptive.runtime

enum class PlatformType {

    JVM_MAC,
    JVM_WIN,
    JVM_LINUX,
    JVM_OTHER,
    JS_BROWSER_MAC,
    JS_BROWSER_WIN,
    JS_BROWSER_LINUX,
    JS_OTHER,
    ANDROID,
    IOS,
    OTHER;

    val isJvm get() = (this in JVM_MAC .. JVM_OTHER)
    val isJs get() = (this in JS_BROWSER_MAC .. JS_OTHER)

    val isMac get() = (this == JVM_MAC || this == JS_BROWSER_MAC)
    val isWin get() = (this == JVM_WIN || this == JS_BROWSER_WIN)
    val isLinux get() = (this == JVM_LINUX || this == JS_BROWSER_LINUX)

    val isIos get() = this == IOS
    val isAndroid get() = this == ANDROID
}