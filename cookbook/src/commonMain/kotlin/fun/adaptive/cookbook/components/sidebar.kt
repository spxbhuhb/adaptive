package `fun`.adaptive.cookbook.components

import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.assignment
import `fun`.adaptive.cookbook.folder
import `fun`.adaptive.cookbook.grid_view
import `fun`.adaptive.cookbook.mail
import `fun`.adaptive.cookbook.shared.colors
import `fun`.adaptive.cookbook.shared.hoverStyles
import `fun`.adaptive.cookbook.shared.lightGray
import `fun`.adaptive.cookbook.shared.normalStyles
import `fun`.adaptive.cookbook.shared.primaryStyles
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.Name
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.api.lineHeight
import `fun`.adaptive.ui.api.navClick
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp

@Adaptive
fun sidebar() {
    column {
        for (item in items.sortedBy { it.index }) {
            sideBarItem(item) .. navClick(mainContent) { text(item.title) }
        }
    }
}

@Adaptive
private fun sideBarItem(
    item: SideBarItem,
    vararg instructions: AdaptiveInstruction
): AdaptiveFragment {

    val hover = hover()
    val colors = colors(item.active, hover)

    row(*instructions, *itemStyles, *colors) {
        svg(item.icon) .. iconStyles .. colors
        text(item.title) .. fontSize(16.sp) .. lineHeight(22.dp) .. colors
    }

    return fragment()
}

private class SideBarItem(
    val index: Int,
    val icon: DrawableResource,
    val title: String,
    val active: Boolean
)

private val items = setOf(
    SideBarItem(0, Res.drawable.grid_view, "Zónák", false),
    SideBarItem(1, Res.drawable.mail, "Értesítések", true),
    SideBarItem(2, Res.drawable.folder, "Beállítások", false),
    SideBarItem(3, Res.drawable.assignment, "Elemzések", false),
)

private val mainContent = Name("Main Content")

private val iconStyles = size(24.dp, 24.dp)
private val itemStyles = size(314.dp, 63.dp) .. alignItems.startCenter .. gap(16.dp) .. paddingLeft(32.dp)