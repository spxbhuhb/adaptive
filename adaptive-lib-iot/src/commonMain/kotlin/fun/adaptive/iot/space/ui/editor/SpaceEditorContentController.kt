package `fun`.adaptive.iot.space.ui.editor

import `fun`.adaptive.adaptive_lib_iot.generated.resources.renameFail
import `fun`.adaptive.adaptive_lib_iot.generated.resources.renameSuccess
import `fun`.adaptive.adaptive_lib_iot.generated.resources.saveFail
import `fun`.adaptive.adaptive_lib_iot.generated.resources.saveSuccess
import `fun`.adaptive.iot.space.AioSpaceApi
import `fun`.adaptive.iot.space.AioSpaceSpec
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem
import `fun`.adaptive.value.ui.iconFor

class SpaceEditorContentController(
    override val workspace: Workspace
) : WsPaneController<AvItem<AioSpaceSpec>>(), WithWorkspace {

    val spaceService = getService<AioSpaceApi>(transport)

    override fun accepts(pane: WsPaneType<AvItem<AioSpaceSpec>>, modifiers: Set<EventModifier>, item: WsItem): Boolean {
        return (item is AvItem<*>) && (SpaceMarkers.SPACE in item.markers)
    }

    override fun load(pane: WsPaneType<AvItem<AioSpaceSpec>>, modifiers: Set<EventModifier>, item: WsItem): WsPaneType<AvItem<AioSpaceSpec>> {
        val spaceItem = item.asAvItem<AioSpaceSpec>()
        return pane.copy(
            name = item.name,
            data = spaceItem.asAvItem(),
            icon = iconFor(spaceItem)
        )
    }

    fun rename(spaceId: AvValueId, name: String) {
        remote(Strings.renameSuccess, Strings.renameFail) {
            spaceService.rename(spaceId, name)
        }
    }

    fun setSpaceSpec(spaceId: AvValueId, spec : AioSpaceSpec) {
        remote(Strings.saveSuccess, Strings.saveFail) {
            spaceService.setSpecSpec(spaceId, spec)
        }
    }
}