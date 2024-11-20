package `fun`.adaptive.cookbook.ui.sidebar

import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.navigation.open

private fun next() {
    val current = navState.frontend.value
    val nextIndex = items.indexOfFirst { it.state == current } + 1
    val nextItem = if (nextIndex < items.size) items[nextIndex] else items[0]
    navState.open(nextItem.state)
}

@Adaptive
fun sidebarContent(vararg instructions: AdaptiveInstruction) : AdaptiveFragment {
    val navState = autoItemOrigin(navState)

    row(*instructions) {
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

