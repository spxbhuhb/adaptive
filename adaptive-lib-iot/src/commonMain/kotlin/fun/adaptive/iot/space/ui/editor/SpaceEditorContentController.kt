package `fun`.adaptive.iot.space.ui.editor

import `fun`.adaptive.adaptive_lib_iot.generated.resources.renameFail
import `fun`.adaptive.adaptive_lib_iot.generated.resources.renameSuccess
import `fun`.adaptive.adaptive_lib_iot.generated.resources.saveFail
import `fun`.adaptive.adaptive_lib_iot.generated.resources.saveSuccess
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.iot.space.AioSpaceApi
import `fun`.adaptive.iot.space.markers.AmvSpace
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.value.ui.iconFor

class SpaceEditorContentController(
    override val workspace: Workspace
) : WsPaneController<AvItem>(), WithWorkspace {

    val spaceService = getService<AioSpaceApi>(transport)

    override fun accepts(pane: WsPaneType<AvItem>, modifiers: Set<EventModifier>, item: WsItem): Boolean {
        return (item is AvItem) && (SpaceMarkers.SPACE in item.markers)
    }

    override fun load(pane: WsPaneType<AvItem>, modifiers: Set<EventModifier>, item: WsItem): WsPaneType<AvItem> {
        return pane.copy(
            name = item.name,
            data = item as AvItem,
            icon = iconFor(item)
        )
    }

    fun rename(spaceId: AvValueId, name: String) {
        remote(Strings.renameSuccess, Strings.renameFail) {
            spaceService.rename(spaceId, name)
        }
    }

    fun setSpaceData(space: AmvSpace) {
        remote(Strings.saveSuccess, Strings.saveFail) {
            spaceService.setSpaceData(space.uuid, space.area, space.notes)
        }
    }
}