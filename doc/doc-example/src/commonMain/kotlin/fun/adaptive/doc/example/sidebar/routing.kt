package `fun`.adaptive.doc.example.sidebar

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.doc.example.generated.resources.assignment
import `fun`.adaptive.doc.example.generated.resources.grid_view
import `fun`.adaptive.doc.example.generated.resources.mail
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.utility.Url
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.navState
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem

val sidebarRecipeNavState = storeFor { NavState(Url()) }

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
    SidebarItem(Graphics.settings, "Settings", Routes.settings),
    SidebarItem(Graphics.assignment, "Reports", Routes.reports),
    SidebarItem(Graphics.assignment, "Networks", Routes.networks)
)