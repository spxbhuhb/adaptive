package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.auth.authRouting
import `fun`.adaptive.cookbook.ui.navigation.navRouting
import `fun`.adaptive.ui.app.basic.BasicAppData
import `fun`.adaptive.ui.navigation.navState
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem

val appData = BasicAppData().apply {

    this.appName = "Adaptive"

    this.largeAppIcon = Res.drawable.eco
    this.mediumAppIcon = Res.drawable.eco

    this.loginPage = Routes.login
    this.publicLanding = Routes.publicLanding
    this.memberLanding = Routes.memberLanding

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
    val box = navState("box", title = "Box")
    val canvas = navState("canvas", title = "Canvas")
    val dialog = navState("dialog", title = "Dialog")
    val editor = navState("editor", title = "Editor")
    val empty = navState("empty", title = "Empty")
    val event = navState("event", title = "Event")
    val form = navState("form", title = "Form")
    val grid = navState("grid", title = "Grid")
    val login = navState("login", title = "Login", fullScreen = true)
    val navigation = navRouting // No title as it's a routing function, not a state
    val publicLanding = navState("public-landing", title = "Cookbook")
    val memberLanding = navState("member-landing", title = "Cookbook")
    val responsive = navState("responsive", title = "Responsive")
    val select = navState("select", title = "Select")
    val sidebar = navState("sideBar", title = "SideBar")
    val snackbar = navState("snackbar", title = "Snackbar")
    val svg = navState("svg", title = "SVG")
    val text = navState("text", title = "Text")
    val tree = navState("tree", title = "Tree")
}

