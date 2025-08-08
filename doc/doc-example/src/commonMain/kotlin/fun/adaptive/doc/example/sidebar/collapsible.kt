package `fun`.adaptive.doc.example.sidebar

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.doc.example.generated.resources.eco
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
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

private val sidebarState = storeFor { SidebarState(FULL) }

@Adaptive
fun collapsible() {
    val state = observe { sidebarState }

    box {
        containerStyles

        when (state.mode) {
            MENU_CLOSED -> closedMenu()
            MENU_OPEN -> openMenu()
            THIN -> thinMode()
            FULL -> fullMode()
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
        fullSidebar(items, sidebarRecipeNavState) .. gridPos(2, 1)
        sidebarContent() .. gridPos(1, 2) .. rowSpan(2)
    }
}

@Adaptive
private fun fullHeader(vararg instructions : AdaptiveInstruction) : AdaptiveFragment {
    grid(instructions()) {
        padding(16.dp, 0.dp, 16.dp, 32.dp)
        colTemplate(64.dp, 1.fr)
        alignItems.startCenter
        onClick { sidebarState.value = sidebarState.value.copy(mode = THIN) }

        box {
            size(48.dp, 48.dp)
            svg(Graphics.eco) .. svgHeight(48.dp) .. svgWidth(48.dp) .. fill(colors.onSurface)
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
        thinSidebar(items, sidebarRecipeNavState) .. gridPos(2, 1)
        sidebarContent() .. gridPos(1, 2) .. rowSpan(2)
    }
}

@Adaptive
fun thinHeader(vararg instructions : AdaptiveInstruction) : AdaptiveFragment {
    box(instructions()) {
        size(80.dp, 80.dp) .. alignItems.center
        onClick { sidebarState.value = sidebarState.value.copy(mode = FULL) }

        box {
            size(48.dp, 48.dp)
            svg(Graphics.eco) .. svgHeight(48.dp) .. svgWidth(48.dp) .. fill(colors.onSurface)
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

