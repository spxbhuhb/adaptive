package `fun`.adaptive.cookbook.ui.sidebar

import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.assignment
import `fun`.adaptive.cookbook.folder
import `fun`.adaptive.cookbook.grid_view
import `fun`.adaptive.cookbook.mail
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem

val sidebarRecipeNavState = autoItemOrigin(NavState())

object Routes {
    val zones = NavState("zones")
    val notifications = NavState("notifications")
    val settings = NavState("settings")
    val reports = NavState("reports")
    val networks = NavState("networks")
}

val items = listOf(
    SidebarItem(Res.drawable.grid_view, "Zones", Routes.zones),
    SidebarItem(Res.drawable.mail, "Notifications", Routes.notifications),
    SidebarItem(Res.drawable.folder, "Settings", Routes.settings),
    SidebarItem(Res.drawable.assignment, "Reports", Routes.reports),
    SidebarItem(Res.drawable.assignment, "Networks", Routes.networks)
)