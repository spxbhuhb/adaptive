package `fun`.adaptive.runtime

object GlobalRuntimeContext {

    val platform
        get() = getPlatformType()

    // FIXME replace hard-coded dev mode with an environment variable or a setting
    val devMode = false

}