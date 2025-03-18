package `fun`.adaptive.runtime

object GlobalRuntimeContext {

    val isClient
        get() = nodeType == ApplicationNodeType.Client

    val isServer
        get() = nodeType == ApplicationNodeType.Server

    var nodeType: ApplicationNodeType? = null

}