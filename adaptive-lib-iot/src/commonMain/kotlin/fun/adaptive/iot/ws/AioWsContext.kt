package `fun`.adaptive.iot.ws

import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.item.AioItemApi
import `fun`.adaptive.iot.item.AioItemId
import `fun`.adaptive.iot.value.AioValueApi
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext

class AioWsContext(override val workspace: Workspace) : WsContext {

    val valueService = getService<AioValueApi>(workspace.transport)
    val itemService = getService<AioItemApi>(workspace.transport)

    val spaceMap = mutableMapOf<AioItemId, AioItem>()

    init {
        //io { initSpaces(this) }
    }

//    val infrastructureMap = mutableMapOf<AioInfrastructureItemId, AioInfrastructureItem>()
//
//    val infrastructureTree = TreeViewModel<AioInfrastructureItem, AioWsContext>(
//        emptyList(),
//        multiSelect = false,
//        context = this
//    )
//
//    init {
//        io { initInfrastructure(this) }
//    }
//

//    fun addSpace(parentItem: TreeItem<AioSpace>?, itemId: AioSpaceId?, site: AioSpaceType, displayOrder: Int) {
//        io {
//            val space = spaceService.add(projectId, itemId, site, displayOrder)
//
//            spaceMap[space.uuid] = space
//
//            val newItem = space.toTreeItem(parentItem)
//
//            if (parentItem != null) {
//                parentItem.children += newItem
//                if (! parentItem.open) parentItem.open = true
//            } else {
//                //spaceTree.items += newItem
//            }
//        }
//    }

//    fun updateSpace(space: AioSpace) {
//        io {
//            spaceService.update(space)
//            spaceMap[space.uuid] = space
//        }
//    }

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

        const val WSPANE_INFRASTRUCTURE_TOOL = "aio:infrastructure:tool"
        const val WSPANE_INFRASTRUCTURE_CONTENT = "aio:infrastructure:content"

        const val WSPANE_MEASUREMENT_LOCATION_TOOL = "aio:measurement:location:tool"
        const val WSPANE_MEASUREMENT_LOCATION_CONTENT = "aio:measurement:location:content"
    }

}