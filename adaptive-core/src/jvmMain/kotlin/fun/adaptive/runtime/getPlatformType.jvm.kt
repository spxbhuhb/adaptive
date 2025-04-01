package `fun`.adaptive.runtime

actual fun getPlatformType(): PlatformType {
    val vmName = System.getProperty("java.vm.name") ?: ""

    if (vmName.contains("Dalvik") || vmName.contains("ART")) {
        return PlatformType.ANDROID
    } else {
        return PlatformType.JVM_OTHER
    }
}