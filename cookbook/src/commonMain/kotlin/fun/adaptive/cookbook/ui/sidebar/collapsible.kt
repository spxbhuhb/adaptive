package `fun`.adaptive.cookbook.ui.sidebar

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.eco
import `fun`.adaptive.cookbook.grid_view
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.boldFont
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.gridPos
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingTop
import `fun`.adaptive.ui.api.rowSpan
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.navigation.sidebar.fullSidebar
import `fun`.adaptive.ui.navigation.sidebar.theme.fullSidebarTheme
import `fun`.adaptive.ui.navigation.sidebar.theme.thinSidebarTheme
import `fun`.adaptive.ui.navigation.sidebar.thinSidebar
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors

const val MENU_CLOSED = "menu_closed"
const val MENU_OPEN = "menu_open"
const val THIN = "thin"
const val FULL = "full"

@Adat
private class SidebarState(
    val mode: String
)

private val sidebarState = autoInstance(SidebarState(FULL), trace = true)

@Adaptive
fun collapsible() {
    val state = autoInstance(sidebarState, trace = true)

    box {
        size(800.dp, 600.dp) .. backgrounds.friendly
        icon(Res.drawable.grid_view)

        when (state?.mode) {
            MENU_CLOSED -> closedMenu()
            MENU_OPEN -> openMenu()
            THIN -> thinMode()
            FULL -> fullMode()
            null -> fullMode()
        }
    }
}

@Adaptive
fun fullMode() {
    grid {
        maxHeight .. backgrounds.surface .. width { fullSidebarTheme.width }

        rowTemplate(100.dp, 1.fr)
        colTemplate(fullSidebarTheme.width, 1.fr)

        fullHeader() .. gridPos(1,1)
        fullSidebar(items, navState) .. gridPos(2, 1)
        sidebarContent()  .. gridPos(1, 2) .. rowSpan(2)
    }
}

@Adaptive
private fun fullHeader(vararg instructions : AdaptiveInstruction) : AdaptiveFragment {
    grid(*instructions) {
        padding(16.dp, 0.dp, 16.dp, 32.dp)
        colTemplate(64.dp, 1.fr)
        alignItems.startCenter
        onClick { sidebarState.frontend.update(SidebarState(THIN)) }

        box {
            size(48.dp, 48.dp)
            svg(Res.drawable.eco) .. svgHeight(48.dp) .. svgWidth(48.dp) .. svgFill(colors.onSurface)
        }

        text("Adaptive") .. boldFont .. fontSize(28.sp) .. paddingTop { 8.dp }
    }

    return fragment()
}

@Adaptive
fun thinMode() {
    grid {
        maxHeight .. backgrounds.surface .. width { thinSidebarTheme.width }

        rowTemplate(80.dp, 1.fr)
        colTemplate(thinSidebarTheme.width, 1.fr)

        thinHeader() .. gridPos(1,1)
        thinSidebar(items, navState) .. gridPos(2, 1)
        sidebarContent()  .. gridPos(1, 2) .. rowSpan(2)
    }
}

@Adaptive
fun thinHeader(vararg instructions : AdaptiveInstruction) : AdaptiveFragment {
    box(*instructions) {
        size(80.dp, 80.dp) .. alignItems.center
        onClick { sidebarState.frontend.update(SidebarState(FULL)) }

        box {
            size(48.dp, 48.dp)
            svg(Res.drawable.eco) .. svgHeight(48.dp) .. svgWidth(48.dp) .. svgFill(colors.onSurface)
        }
    }
    return fragment()
}

@Adaptive
fun closedMenu() {

}

@Adaptive
fun openMenu() {
}

