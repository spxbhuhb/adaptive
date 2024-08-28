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
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.lineHeight
import `fun`.adaptive.ui.api.navClick
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp

val mainContent = Name("Main Content")

@Adaptive
fun sidebar() {
    column {
        sideBarItem(Res.drawable.grid_view, "Zónák") .. navClick(mainContent) { text("Zónák") }
        sideBarItem(Res.drawable.mail, "Értesítések") .. navClick(mainContent) { text("Értesítések") }
        sideBarItem(Res.drawable.folder, "Beállítások") .. navClick(mainContent) { text("Beállítások") }
        sideBarItem(Res.drawable.assignment, "Elemzések") .. navClick(mainContent) { text("Elemzések") }
    }
}

private val iconStyles = size(24.dp, 24.dp)
private val itemStyles = size(314.dp, 63.dp) .. alignItems.startCenter .. gap(16.dp) .. paddingLeft(32.dp)

@Adaptive
private fun sideBarItem(
    icon: DrawableResource,
    title: String,
    vararg instructions: AdaptiveInstruction
): AdaptiveFragment {

    row(*instructions, *itemStyles) {
        svg(icon) .. iconStyles
        text(title) .. fontSize(16.sp) .. lineHeight(22.dp)
    }

    return fragment()
}