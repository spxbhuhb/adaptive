package `fun`.adaptive.iot.ws

import `fun`.adaptive.iot.space.AioSpaceApi
import `fun`.adaptive.iot.space.AioSpaceId
import `fun`.adaptive.iot.project.model.AioProject
import `fun`.adaptive.iot.space.model.AioSpace
import `fun`.adaptive.iot.space.model.AioSpaceType
import `fun`.adaptive.iot.space.ui.SpaceTreeModel
import `fun`.adaptive.iot.space.ui.initSpaces
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext
import `fun`.adaptive.utility.UUID
import kotlin.collections.plus

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

    fun addSpace(parentItem: TreeItem<AioSpace>?, itemId: AioSpaceId?, site: AioSpaceType, displayOrder: Int) {
        io {
            val space = spaceService.add(projectId, itemId, site, displayOrder)

            spaceMap[space.uuid] = space

            val newItem = space.toTreeItem(parentItem)

            if (parentItem != null) {
                parentItem.children += newItem
                if (! parentItem.open) parentItem.open = true
            } else {
                spaceTree.items += newItem
            }
        }
    }

    fun updateSpace(space: AioSpace) {
        io {
            spaceService.update(space)
            spaceMap[space.uuid] = space
        }
    }

    companion object {
        const val WSIT_INFRASTRUCTURE_ITEM = "aio:infrastructure:item"
        const val WSIT_NETWORK = "aio:network"
        const val WSIT_DEVICE = "aio:device"
        const val WSIT_POINT = "aio:point"

        const val WSIT_MEASUREMENT_LOCATION = "aio:measurement:point"
        const val WSIT_MEASUREMENT_POINT = "aio:measurement:point"
        const val WSIT_PROJECT = "aio:project"
        const val WSIT_SPACE = "aio:space"

        const val WSPANE_SPACE_TOOL = "aio:space:tool"
        const val WSPANE_SPACE_CONTENT = "aio:space:content"

        const val WSPANE_MEASUREMENT_LOCATION_TOOL = "aio:measurement:location:tool"
        const val WSPANE_MEASUREMENT_LOCATION_CONTENT = "aio:measurement:location:content"
    }

}