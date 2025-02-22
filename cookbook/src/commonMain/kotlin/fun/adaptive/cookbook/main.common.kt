package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.auth.authRouting
import `fun`.adaptive.cookbook.ui.navigation.navRouting
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.app.basic.BasicAppData
import `fun`.adaptive.ui.navigation.navState
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem

val appData = BasicAppData().apply {

    this.appName = "Adaptive"

    this.largeAppIcon = Graphics.eco
    this.mediumAppIcon = Graphics.eco

    this.loginPage = Routes.login
    this.publicLanding = Routes.publicLanding
    this.memberLanding = Routes.memberLanding

    this.sidebarItems = listOf(
        SidebarItem(Graphics.grid_view, "Auth", Routes.auth),
        SidebarItem(Graphics.grid_view, "Box", Routes.box),
        SidebarItem(Graphics.grid_view, "Canvas", Routes.canvas),
        SidebarItem(Graphics.grid_view, "Dialog", Routes.dialog),
        SidebarItem(Graphics.grid_view, "Editor", Routes.editor),
        SidebarItem(Graphics.grid_view, "Event", Routes.event),
        SidebarItem(Graphics.grid_view, "Form", Routes.form),
        SidebarItem(Graphics.grid_view, "Good Morning", Routes.goodMorning),
        SidebarItem(Graphics.grid_view, "Grid", Routes.grid),
        SidebarItem(Graphics.grid_view, "Navigation", Routes.navigation),
        SidebarItem(Graphics.grid_view, "Popup", Routes.popup),
        SidebarItem(Graphics.grid_view, "Select", Routes.select),
        SidebarItem(Graphics.grid_view, "Sidebar", Routes.sidebar),
        SidebarItem(Graphics.grid_view, "Snackbar", Routes.snackbar),
        SidebarItem(Graphics.grid_view, "SplitPane", Routes.splitPane),
        SidebarItem(Graphics.grid_view, "SVG", Routes.svg),
        SidebarItem(Graphics.grid_view, "Text", Routes.text),
        SidebarItem(Graphics.grid_view, "Tree", Routes.tree)
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
    val goodMorning = navState("goodMorning", title = "Good Morning")
    val grid = navState("grid", title = "Grid")
    val login = navState("login", title = "Login", fullScreen = true)
    val navigation = navRouting // No title as it's a routing function, not a state
    val publicLanding = navState("public-landing", title = "Cookbook")
    val memberLanding = navState("member-landing", title = "Cookbook")
    val popup = navState("popup", title = "Popup")
    val select = navState("select", title = "Select")
    val sidebar = navState("sideBar", title = "SideBar")
    val snackbar = navState("snackbar", title = "Snackbar")
    val splitPane = navState("splitPane", title = "SplitPane")
    val svg = navState("svg", title = "SVG")
    val text = navState("text", title = "Text")
    val tree = navState("tree", title = "Tree")
}

