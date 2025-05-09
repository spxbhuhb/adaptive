package `fun`.adaptive.iot.space.ui.editor

import `fun`.adaptive.iot.generated.resources.renameFail
import `fun`.adaptive.iot.generated.resources.renameSuccess
import `fun`.adaptive.iot.generated.resources.saveFail
import `fun`.adaptive.iot.generated.resources.saveSuccess
import `fun`.adaptive.iot.space.AioSpaceApi
import `fun`.adaptive.iot.space.AioSpaceSpec
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.value.iconFor
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValue.Companion.asAvValue

class SpaceEditorContentController(
    override val workspace: Workspace
) : WsPaneController<AvValue<AioSpaceSpec>>() {

    val spaceService = getService<AioSpaceApi>(transport)

    override fun accepts(pane: WsPaneType<AvValue<AioSpaceSpec>>, modifiers: Set<EventModifier>, item: NamedItem): Boolean {
        return (item is AvValue<*>) && (SpaceMarkers.SPACE in item.markers)
    }

    override fun load(pane: WsPaneType<AvValue<AioSpaceSpec>>, modifiers: Set<EventModifier>, item: NamedItem): WsPaneType<AvValue<AioSpaceSpec>> {
        val spaceItem = item.asAvValue<AioSpaceSpec>()
        return pane.copy(
            name = item.name,
            data = spaceItem.asAvValue(),
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