package `fun`.adaptive.iot.ws

import `fun`.adaptive.iot.api.AioSpaceApi
import `fun`.adaptive.iot.model.project.AioProject
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.iot.ui.space.SpaceTreeModel
import `fun`.adaptive.iot.ui.space.initSpaces
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext
import `fun`.adaptive.utility.UUID

class AioWsContext(override val workspace: Workspace) : WsContext {

    val spaceService = getService<AioSpaceApi>(workspace.transport)

    val projectId = UUID<AioProject>()

    val spaceMap = mutableMapOf<UUID<AioSpace>, AioSpace>()

    val spaceTree = TreeViewModel<AioSpace, AioWsContext>(
        emptyList(),
        selectedFun = ::spaceToolSelectedFun,
        multiSelect = false,
        context = this
    )

    init {
        io {
            initSpaces(this)
        }
    }

    fun spaceToolSelectedFun(viewModel: SpaceTreeModel, item: TreeItem<AioSpace>, modifiers: Set<EventModifier>) {
        workspace.addContent(item.data, modifiers)
        TreeViewModel.defaultSelectedFun(viewModel, item, modifiers)
    }

    fun updateSpace(space: AioSpace) {
        io { spaceService.update(space) }
    }

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