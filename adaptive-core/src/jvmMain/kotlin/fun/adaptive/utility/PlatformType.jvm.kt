package `fun`.adaptive.utility

actual val platformType: PlatformType
    get() {
        val vmName = System.getProperty("java.vm.name") ?: ""
        if (vmName.contains("Dalvik") || vmName.contains("ART")) {
            return PlatformType.Android
        } else {
            return PlatformType.JVM
        }
    }