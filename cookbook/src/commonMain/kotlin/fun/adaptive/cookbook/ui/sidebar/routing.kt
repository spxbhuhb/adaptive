package `fun`.adaptive.cookbook.ui.sidebar

import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.cookbook.assignment
import `fun`.adaptive.cookbook.folder
import `fun`.adaptive.cookbook.grid_view
import `fun`.adaptive.cookbook.mail
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.navState
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem

val sidebarRecipeNavState = autoItemOrigin(NavState())

object Routes {
    val zones = navState("zones")
    val notifications = navState("notifications")
    val settings = navState("settings")
    val reports = navState("reports")
    val networks = navState("networks")
}

val items = listOf(
    SidebarItem(Graphics.grid_view, "Zones", Routes.zones),
    SidebarItem(Graphics.mail, "Notifications", Routes.notifications),
    SidebarItem(Graphics.folder, "Settings", Routes.settings),
    SidebarItem(Graphics.assignment, "Reports", Routes.reports),
    SidebarItem(Graphics.assignment, "Networks", Routes.networks)
)