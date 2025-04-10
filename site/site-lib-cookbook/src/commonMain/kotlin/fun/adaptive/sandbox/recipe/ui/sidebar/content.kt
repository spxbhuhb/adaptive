package `fun`.adaptive.cookbook.recipe.ui.sidebar

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp

private fun next() {
    val current = sidebarRecipeNavState.value
    val nextIndex = items.indexOfFirst { it.state == current } + 1
    val nextItem = if (nextIndex < items.size) items[nextIndex] else items[0]
    sidebarRecipeNavState.value = nextItem.state
}

@Adaptive
fun sidebarContent() : AdaptiveFragment {
    val navState = valueFrom { sidebarRecipeNavState }

    row(instructions()) {
        padding { 16.dp }

        when (navState) {
            in Routes.zones -> innerContent("zones")
            in Routes.notifications -> innerContent("notifications")
            in Routes.settings -> innerContent("settings")
            in Routes.reports -> innerContent("reports")
            in Routes.networks -> innerContent("networks")
            else -> text("no item is selected, click on the left")
        }
    }

    return fragment()
}

@Adaptive
private fun innerContent(selection : String) {
    text("selected: $selection (click on this text to open the next)") .. onClick { next() } .. noSelect
}

