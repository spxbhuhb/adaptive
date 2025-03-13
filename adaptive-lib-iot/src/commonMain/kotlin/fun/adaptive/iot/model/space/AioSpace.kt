package `fun`.adaptive.iot.model.space

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.model.project.AioProjectId
import `fun`.adaptive.iot.model.project.AioProjectItem
import `fun`.adaptive.iot.model.project.FriendlyItemId
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.utility.UUID

@Adat
data class AioSpace(
    override val uuid: AioSpaceId,
    override val projectId: AioProjectId,
    override val friendlyId: FriendlyItemId,
    override val name: String,
    val type: AioSpaceType,
    val notes: String = "",
    val active: Boolean = true,
    val area: Double? = null,
    val parentSpace: UUID<AioSpace>? = null
) : AioProjectItem<AioSpace>() {

    fun toTreeItem(parent: TreeItem<AioSpace>?) = TreeItem(
        icon = icon(),
        title = name,
        data = this,
        parent = parent
    )

    fun icon() =
        when (type) {
            AioSpaceType.Site -> Graphics.responsive_layout
            AioSpaceType.Building -> Graphics.apartment
            AioSpaceType.Floor -> Graphics.stacks
            AioSpaceType.Room -> Graphics.meeting_room
            AioSpaceType.Area -> Graphics.crop_5_4
        }

}