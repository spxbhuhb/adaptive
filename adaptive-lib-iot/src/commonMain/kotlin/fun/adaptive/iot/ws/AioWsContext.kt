package `fun`.adaptive.iot.ws

import `fun`.adaptive.iot.model.project.AioProject
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.tree.TreeViewModel.Companion.defaultSelectedFun
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext
import `fun`.adaptive.utility.UUID

class AioWsContext(override val workspace: Workspace) : WsContext {

    val spaceTree = TreeViewModel<AioSpace, AioProject>(
        emptyList(),
        selectedFun = ::defaultSelectedFun,
        multiSelect = false,
        context = AioProject(UUID())
    )

    companion object {
        const val WSIT_DEVICE = "aio:device"
        const val WSIT_DEVICE_POINT = "aio:device:point"
        const val WSIT_MEASUREMENT_LOCATION = "aio:measurement:point"
        const val WSIT_MEASUREMENT_POINT = "aio:measurement:point"
        const val WSIT_NETWORK = "aio:network"
        const val WSIT_PROJECT = "aio:project"
        const val WSIT_SPACE = "aio:space"

        const val WSPANE_SPACE_TOOL = "aio:space:tool"
        const val WSPANE_SPACE_CONTENT = "aio:space:content"
    }

}