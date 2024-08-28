package `fun`.adaptive.cookbook.components

import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.assignment
import `fun`.adaptive.cookbook.folder
import `fun`.adaptive.cookbook.grid_view
import `fun`.adaptive.cookbook.mail
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.Name
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.lineHeight
import `fun`.adaptive.ui.api.navClick
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp

private val mainContent = Name("Main Content")

private class SideBarItem(
    val index: Int,
    val icon: DrawableResource,
    val title: String,
    val active: Boolean
)

private val activeBackground = backgroundColor(0x6259CE)
private val normalBackground = backgroundColor(0xffffff)

private val activeTextColor = textColor(0xffffff)
private val normalTextColor = textColor(0x0)

private val activeIconColor = svgFill(0xffffff)
private val normalIconColor = svgFill(0x0)

private val items = setOf(
    SideBarItem(0, Res.drawable.grid_view, "Zónák", false),
    SideBarItem(1, Res.drawable.mail, "Értesítések", true),
    SideBarItem(2, Res.drawable.folder, "Beállítások", false),
    SideBarItem(3, Res.drawable.assignment, "Elemzések", false),
)

@Adaptive
fun sidebar() {
    column {
        for (item in items.sortedBy { it.index }) {
            sideBarItem(item) .. navClick(mainContent) { text(item.title) }
        }
    }
}

private val iconStyles = size(24.dp, 24.dp)
private val itemStyles = size(314.dp, 63.dp) .. alignItems.startCenter .. gap(16.dp) .. paddingLeft(32.dp)

@Adaptive
private fun sideBarItem(
    item: SideBarItem,
    vararg instructions: AdaptiveInstruction
): AdaptiveFragment {

    row(*instructions, *itemStyles, if (item.active) activeBackground else normalBackground) {
        svg(item.icon) .. iconStyles .. if (item.active) activeIconColor else normalIconColor
        text(item.title) .. fontSize(16.sp) .. lineHeight(22.dp) .. if (item.active) activeTextColor else normalTextColor
    }

    return fragment()
}