package `fun`.adaptive.runtime

object GlobalRuntimeContext {

    val isClient
        get() = nodeType == ApplicationNodeType.Client

    val isServer
        get() = nodeType == ApplicationNodeType.Server

    var nodeType: ApplicationNodeType? = null

    val platform
        get() = getPlatformType()

    // FIXME replace hard-coded dev mode with an environment variable or a setting
    val devMode = true

}