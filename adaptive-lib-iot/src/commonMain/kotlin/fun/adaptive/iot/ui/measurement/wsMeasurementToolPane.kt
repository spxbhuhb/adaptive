package `fun`.adaptive.iot.ui.measurement

import `fun`.adaptive.adaptive_lib_iot.generated.resources.dew_point
import `fun`.adaptive.adaptive_lib_iot.generated.resources.temperatureAndHumidity
import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.model.AioSpaceId
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeItemSelectedFun
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.wsToolPane
import `fun`.adaptive.utility.UUID

typealias MeasurementTreeModel = TreeViewModel<MeasurementWsItem, AioWsContext>
private typealias MTreeItem = TreeItem<MeasurementWsItem>

@Adat
class MeasurementWsItem(
    override val name: String,
    override val type: WsItemType = AioWsContext.WSIT_MEASUREMENT_LOCATION,
    val spaceId : AioSpaceId
) : WsItem()

fun wsMeasurementSpaceTree() =
    WsPane(
        UUID(),
        Strings.temperatureAndHumidity,
        Graphics.dew_point,
        WsPanePosition.LeftTop,
        AioWsContext.WSPANE_MEASUREMENT_LOCATION_TOOL,
        model = Unit
    )

@Adaptive
fun wsMeasurementToolPane(): AdaptiveFragment {

    val context = fragment().wsContext<AioWsContext>()
    val observed = valueFrom { transformTreeModel(context, ::measurementToolSelectedFun) }

    wsToolPane(context.pane(AioWsContext.WSPANE_MEASUREMENT_LOCATION_TOOL)) {
        tree(observed)
    }

    return fragment()
}

private fun measurementToolSelectedFun(viewModel: MeasurementTreeModel, item: MTreeItem, modifiers: Set<EventModifier>) {
    val workspace = viewModel.context.workspace
    workspace.addContent(item.data, modifiers)
    TreeViewModel.defaultSelectedFun(viewModel, item, modifiers)
}

fun transformTreeModel(
    context : AioWsContext,
    selectedFun : TreeItemSelectedFun<MeasurementWsItem, AioWsContext>
) : MeasurementTreeModel {

    fun transformItem(original : TreeItem<AioSpace>, newParent : MTreeItem?): MTreeItem {
        TreeItem(
            original.icon,
            original.title,
            MeasurementWsItem(original.data.name, spaceId = original.data.uuid),
            parent = newParent
        ).also { new ->
            new.children = original.children.map { transformItem(it, new) }
            return new
        }
    }

    return context.spaceTree.transform(
        context,
        selectedFun = selectedFun,
        transform = ::transformItem
    )
}
