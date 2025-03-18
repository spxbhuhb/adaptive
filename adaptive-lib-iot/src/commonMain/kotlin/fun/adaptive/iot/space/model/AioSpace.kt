package `fun`.adaptive.iot.space.model

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.common.AioMarkerSet
import `fun`.adaptive.iot.project.AioProjectId
import `fun`.adaptive.iot.space.AioSpaceId
import `fun`.adaptive.iot.project.model.AioProjectItem
import `fun`.adaptive.iot.project.FriendlyItemId
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.workspace.model.WsItemType
import `fun`.adaptive.utility.p04

@Adat
data class AioSpace(
    override val uuid: AioSpaceId,
    override val projectId: AioProjectId,
    override val friendlyId: FriendlyItemId,
    override val name: String,
    override val type: WsItemType = AioWsContext.WSIT_SPACE,
    override val markers : AioMarkerSet = emptySet(),
    val spaceType: AioSpaceType,
    val notes: String = "",
    val displayOrder: Int,
    val active: Boolean = true,
    val area: Double? = null,
    val parentId: AioSpaceId? = null,
) : AioProjectItem<AioSpace>() {

    fun toTreeItem(parent: TreeItem<AioSpace>?) = TreeItem(
        icon = icon(),
        title = name.ifEmpty { Strings.noname },
        data = this,
        parent = parent
    )

    val friendlyDisplayId
        get() = "SP-${friendlyId.p04}"

    fun icon() =
        when (spaceType) {
            AioSpaceType.Site -> Graphics.responsive_layout
            AioSpaceType.Building -> Graphics.apartment
            AioSpaceType.Floor -> Graphics.stacks
            AioSpaceType.Room -> Graphics.meeting_room
            AioSpaceType.Area -> Graphics.crop_5_4
        }

}