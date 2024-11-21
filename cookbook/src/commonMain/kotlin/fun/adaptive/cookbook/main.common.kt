package `fun`.adaptive.cookbook

import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.cookbook.auth.authRecipe
import `fun`.adaptive.cookbook.graphics.canvas.canvasRecipe
import `fun`.adaptive.cookbook.ui.dialog.dialogRecipe
import `fun`.adaptive.cookbook.ui.editor.editorRecipe
import `fun`.adaptive.cookbook.ui.event.eventRecipe
import `fun`.adaptive.cookbook.ui.form.formRecipe
import `fun`.adaptive.cookbook.ui.layout.box.boxRecipe
import `fun`.adaptive.cookbook.ui.layout.grid.gridRecipe
import `fun`.adaptive.cookbook.ui.layout.responsive.responsiveMain
import `fun`.adaptive.cookbook.ui.navigation.navRouting
import `fun`.adaptive.cookbook.ui.navigation.navigationRecipe
import `fun`.adaptive.cookbook.ui.select.selectRecipe
import `fun`.adaptive.cookbook.ui.sidebar.sideBarRecipe
import `fun`.adaptive.cookbook.ui.snackbar.snackbarRecipe
import `fun`.adaptive.cookbook.ui.svg.svgRecipe
import `fun`.adaptive.cookbook.ui.text.textRecipe
import `fun`.adaptive.cookbook.ui.tree.treeRecipe
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.sidebar.SidebarItem

val appNavState = autoInstance(Routes.auth)

object Routes {
    val auth = NavState("Auth")
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