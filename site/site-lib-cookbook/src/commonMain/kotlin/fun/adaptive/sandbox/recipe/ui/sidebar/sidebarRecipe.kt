package `fun`.adaptive.sandbox.recipe.ui.sidebar

import `fun`.adaptive.cookbook.generated.resources.collapsable
import `fun`.adaptive.cookbook.generated.resources.fullNormal
import `fun`.adaptive.cookbook.generated.resources.fullVariant
import `fun`.adaptive.cookbook.generated.resources.thin
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.navigation.sidebar.fullSidebar
import `fun`.adaptive.ui.navigation.sidebar.theme.FullSidebarThemeVariant
import `fun`.adaptive.ui.navigation.sidebar.thinSidebar
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders

val containerStyles = size(800.dp, 600.dp) .. backgrounds.friendlyOpaque .. borders.outline

@Adaptive
fun sideBarRecipe(): AdaptiveFragment {
    column {
        maxHeight .. verticalScroll

        gap { 16.dp }

        text(Strings.collapsable)
        collapsible()

        text(Strings.fullNormal)
        full()

        text(Strings.fullVariant)
        variant()

        text(Strings.thin)
        thin()
    }

    return fragment()
}

@Adaptive
fun full() {
    row {
        containerStyles
        fullSidebar(items, sidebarRecipeNavState)
        sidebarContent()
    }
}

@Adaptive
fun variant() {
    row {
        containerStyles
        fullSidebar(items, sidebarRecipeNavState, FullSidebarThemeVariant())
        sidebarContent()
    }
}

@Adaptive
fun thin() {
    row {
        containerStyles
        thinSidebar(items, sidebarRecipeNavState)
        sidebarContent()
    }
}