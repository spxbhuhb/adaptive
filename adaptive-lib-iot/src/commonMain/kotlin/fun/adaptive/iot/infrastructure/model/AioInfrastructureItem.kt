package `fun`.adaptive.iot.infrastructure.model

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.common.AioMarkerSet
import `fun`.adaptive.iot.infrastructure.AioInfrastructureItemId
import `fun`.adaptive.iot.project.AioProjectId
import `fun`.adaptive.iot.space.AioSpaceId
import `fun`.adaptive.iot.project.model.AioProjectItem
import `fun`.adaptive.iot.project.FriendlyItemId
import `fun`.adaptive.iot.space.model.AioSpaceType
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.workspace.model.WsItemType
import `fun`.adaptive.utility.p04

@Adat
data class AioInfrastructureItem(
    override val uuid: AioInfrastructureItemId,
    override val projectId: AioProjectId,
    override val friendlyId: FriendlyItemId,
    override val name: String,
    override val type: WsItemType = AioWsContext.WSIT_INFRASTRUCTURE_ITEM,
    override val markers : AioMarkerSet = emptySet(),
    val itemType: AioInfrastructureItemType,
    val spaceId: AioSpaceId? = null,
    val parentId: AioInfrastructureItemId? = null,
) : AioProjectItem<AioInfrastructureItem>() {

    fun toTreeItem(parent: TreeItem<AioInfrastructureItem>?) = TreeItem(
        icon = icon(),
        title = name.ifEmpty { Strings.noname },
        data = this,
        parent = parent
    )

    fun icon() =
        when (itemType) {
            AioInfrastructureItemType.Host -> Graphics.host
            AioInfrastructureItemType.Network -> Graphics.account_tree
            AioInfrastructureItemType.Device -> Graphics.memory
            AioInfrastructureItemType.Point -> Graphics.database
        }

}