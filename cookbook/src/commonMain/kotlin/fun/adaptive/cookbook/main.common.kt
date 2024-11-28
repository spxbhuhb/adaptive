package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.auth.authRouting
import `fun`.adaptive.cookbook.ui.navigation.navRouting
import `fun`.adaptive.ui.app.basic.BasicAppData
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem

val appData = BasicAppData().apply {

    this.appName = "Adaptive"

    this.largeAppIcon = Res.drawable.eco
    this.mediumAppIcon = Res.drawable.eco

    this.loginPage = Routes.login

    this.sidebarItems = listOf(
        SidebarItem(Res.drawable.grid_view, "Auth", Routes.auth),
        SidebarItem(Res.drawable.grid_view, "Box", Routes.box),
        SidebarItem(Res.drawable.grid_view, "Canvas", Routes.canvas),
        SidebarItem(Res.drawable.grid_view, "Dialog", Routes.dialog),
        SidebarItem(Res.drawable.grid_view, "Editor", Routes.editor),
        SidebarItem(Res.drawable.grid_view, "Event", Routes.event),
        SidebarItem(Res.drawable.grid_view, "Form", Routes.form),
        SidebarItem(Res.drawable.grid_view, "Grid", Routes.grid),
        SidebarItem(Res.drawable.grid_view, "Navigation", Routes.navigation),
        SidebarItem(Res.drawable.grid_view, "Responsive", Routes.responsive),
        SidebarItem(Res.drawable.grid_view, "Select", Routes.select),
        SidebarItem(Res.drawable.grid_view, "Sidebar", Routes.sidebar),
        SidebarItem(Res.drawable.grid_view, "Snackbar", Routes.snackbar),
        SidebarItem(Res.drawable.grid_view, "SVG", Routes.svg),
        SidebarItem(Res.drawable.grid_view, "Text", Routes.text),
        SidebarItem(Res.drawable.grid_view, "Tree", Routes.tree)
    )
}

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
    val login = NavState("Login", fullScreen = true)
    val navigation = navRouting
    val publicLanding = NavState("PublicLanding")
    val responsive = NavState("Responsive")
    val select = NavState("Select")
    val sidebar = NavState("SideBar")
    val snackbar = NavState("Snackbar")
    val svg = NavState("SVG")
    val text = NavState("Text")
    val tree = NavState("Tree")
}

