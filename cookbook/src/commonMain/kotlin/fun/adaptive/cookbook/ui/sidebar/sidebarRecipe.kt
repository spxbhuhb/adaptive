package `fun`.adaptive.cookbook.ui.sidebar

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.navigation.sidebar.fullSidebar
import `fun`.adaptive.ui.navigation.sidebar.theme.FullSidebarThemeVariant
import `fun`.adaptive.ui.navigation.sidebar.thinSidebar

@Adaptive
fun sideBarRecipe() {
    column {
        maxHeight .. verticalScroll

        gap { 16.dp }

        text("Collapsable")
        collapsible()

        text("Full - Normal")
        full()

        text("Full - Variant")
        variant()

        text("Thin")
        thin()
    }
}

@Adaptive
fun full() {
    row {
        fullSidebar(items, sidebarRecipeNavState)
        sidebarContent()
    }
}

@Adaptive
fun variant() {
    row {
        fullSidebar(items, sidebarRecipeNavState, FullSidebarThemeVariant())
        sidebarContent()
    }
}

@Adaptive
fun thin() {
    row {
        thinSidebar(items, sidebarRecipeNavState)
        sidebarContent()
    }
}