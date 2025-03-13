package `fun`.adaptive.iot.space

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.sandbox.*
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.collapse_all
import `fun`.adaptive.ui.builtin.expand_all
import `fun`.adaptive.ui.builtin.remove
import `fun`.adaptive.ui.fragment.layout.SplitPaneConfiguration
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.contextMenu
import `fun`.adaptive.ui.splitpane.splitPaneDivider
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.tree.TreeViewModel.Companion.defaultSelectedFun
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.utility.UUID

@Adat
data class Space(
    val uuid : UUID<Space>,
    val piid : ProjectItemId,
    val type: SpaceType,
    val name: String,
    val notes : String = "",
    val active: Boolean = true,
    val area : Double? = null,
    val parentSpace : UUID<Space>? = null
) {

    fun toTreeItem(parent : TreeItem<Space>?) = TreeItem(
        icon = icon(),
        title = name,
        data = this,
        parent = parent
    )

    fun icon() =
        when (type) {
            SpaceType.Site -> Graphics.responsive_layout
            SpaceType.Building -> Graphics.apartment
            SpaceType.Floor -> Graphics.stacks
            SpaceType.Room -> Graphics.meeting_room
            SpaceType.Area -> Graphics.crop_5_4
        }

}