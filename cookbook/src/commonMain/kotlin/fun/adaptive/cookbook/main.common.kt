package `fun`.adaptive.cookbook

import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.cookbook.auth.authRouting
import `fun`.adaptive.cookbook.ui.navigation.navRouting
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem

val appNavState = autoItemOrigin(Routes.auth)

object Routes {
    val auth = authRouting
    val box = NavState("Box")
    val canvas = NavState("Canvas")
    val dialog = NavState("Dialog")
    val editor = NavState("Editor")
    val empty = NavState("Empty")
    val event = NavState("Event")
    val form = NavState("Form")
    val grid = NavState("Grid")
    val navigation = navRouting
    val responsive = NavState("Responsive")
    val select = NavState("Select")
    val sidebar = NavState("SideBar")
    val snackbar = NavState("Snackbar")
    val svg = NavState("SVG")
    val text = NavState("Text")
    val tree = NavState("Tree")
}

val items = listOf(
    SidebarItem(0, Res.drawable.grid_view, "Auth", Routes.auth),
    SidebarItem(0, Res.drawable.grid_view, "Box", Routes.box),
    SidebarItem(0, Res.drawable.grid_view, "Canvas", Routes.canvas),
    SidebarItem(0, Res.drawable.grid_view, "Dialog", Routes.dialog),
    SidebarItem(0, Res.drawable.grid_view, "Editor", Routes.editor),
    SidebarItem(0, Res.drawable.grid_view, "Event", Routes.event),
    SidebarItem(0, Res.drawable.grid_view, "Form", Routes.form),
    SidebarItem(0, Res.drawable.grid_view, "Grid", Routes.grid),
    SidebarItem(0, Res.drawable.grid_view, "Navigation", Routes.navigation),
    SidebarItem(0, Res.drawable.grid_view, "Responsive", Routes.responsive),
    SidebarItem(0, Res.drawable.grid_view, "Select", Routes.select),
    SidebarItem(0, Res.drawable.grid_view, "Sidebar", Routes.sidebar),
    SidebarItem(0, Res.drawable.grid_view, "Snackbar", Routes.snackbar),
    SidebarItem(0, Res.drawable.grid_view, "SVG", Routes.svg),
    SidebarItem(0, Res.drawable.grid_view, "Text", Routes.text),
    SidebarItem(0, Res.drawable.grid_view, "Tree", Routes.tree)
)