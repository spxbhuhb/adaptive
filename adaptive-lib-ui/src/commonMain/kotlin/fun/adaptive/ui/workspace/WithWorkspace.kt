package `fun`.adaptive.ui.workspace

interface WithWorkspace {

    val workspace: Workspace

    fun io(suspendFun: suspend () -> Unit) = workspace.io(suspendFun)

    val backend
        get() = workspace.backend

    val transport
        get() = workspace.transport

    val scope
        get() = workspace.scope

}