package `fun`.adaptive.cookbook.ui.sidebar

import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.assignment
import `fun`.adaptive.cookbook.folder
import `fun`.adaptive.cookbook.grid_view
import `fun`.adaptive.cookbook.mail
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.open
import `fun`.adaptive.ui.navigation.sidebar.SideBarItem
import `fun`.adaptive.ui.navigation.sidebar.sidebar

@Adaptive
fun sideBarRecipe() {
    row {
        sidebar(items, navState)
        content()
    }
}

@Adaptive
private fun content() {
    val navState = autoInstance(navState, trace = true)

    row {
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
}

@Adaptive
private fun innerContent(selection : String) {
    text("selected: $selection (click on this text to open the next)") .. onClick { next() } .. noSelect
}

private fun next() {
    val current = navState.frontend.value
    val nextIndex = items.indexOfFirst { it.state == current } + 1
    val nextItem = if (nextIndex < items.size) items[nextIndex] else items[0]
    navState.open(nextItem.state)
}

private val navState = autoInstance(NavState(), trace = true)

private object Routes {
    val zones = NavState("zones")
    val notifications = NavState("notifications")
    val settings = NavState("settings")
    val reports = NavState("reports")
    val networks = NavState("networks")
}

private val items = listOf(
    SideBarItem(0, Res.drawable.grid_view, "Zones", Routes.zones),
    SideBarItem(1, Res.drawable.mail, "Notifications", Routes.notifications),
    SideBarItem(2, Res.drawable.folder, "Settings", Routes.settings),
    SideBarItem(3, Res.drawable.assignment, "Reports", Routes.reports),
    SideBarItem(4, Res.drawable.assignment, "Networks", Routes.networks)
)