package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.app.landing
import `fun`.adaptive.cookbook.auth.authRecipe
import `fun`.adaptive.cookbook.auth.ui.responsive.signIn
import `fun`.adaptive.cookbook.goodmorning.goodMorning
import `fun`.adaptive.cookbook.graphics.canvas.canvasRecipe
import `fun`.adaptive.cookbook.ui.dialog.dialogRecipe
import `fun`.adaptive.cookbook.ui.editor.editorRecipe
import `fun`.adaptive.cookbook.ui.event.eventRecipe
import `fun`.adaptive.cookbook.ui.form.formRecipe
import `fun`.adaptive.cookbook.ui.layout.box.boxRecipe
import `fun`.adaptive.cookbook.ui.layout.grid.gridRecipe
import `fun`.adaptive.cookbook.ui.navigation.navigationRecipe
import `fun`.adaptive.cookbook.ui.popup.popupRecipe
import `fun`.adaptive.cookbook.ui.select.selectRecipe
import `fun`.adaptive.cookbook.ui.sidebar.sideBarRecipe
import `fun`.adaptive.cookbook.ui.snackbar.snackbarRecipe
import `fun`.adaptive.cookbook.ui.splitpane.splitPaneRecipe
import `fun`.adaptive.cookbook.ui.svg.svgRecipe
import `fun`.adaptive.cookbook.ui.text.textRecipe
import `fun`.adaptive.cookbook.ui.tree.treeRecipe
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object CookbookFragmentFactory : FoundationFragmentFactory() {
    init {
        add("cookbook:dialog", ::dialogRecipe)
        add("cookbook:auth", ::authRecipe)
        add("cookbook:box", ::boxRecipe)
        add("cookbook:canvas", ::canvasRecipe)
        add("cookbook:dialog", ::dialogRecipe)
        add("cookbook:editor", ::editorRecipe)
        add("cookbook:event", ::eventRecipe)
        add("cookbook:form", ::formRecipe)
        add("cookbook:goodMorning", ::goodMorning)
        add("cookbook:grid", ::gridRecipe)
        add("cookbook:login", ::signIn)
        add("cookbook:navigation", ::navigationRecipe)
        add("cookbook:publicLanding", ::landing)
        add("cookbook:memberLanding", ::landing)
        add("cookbook:popup", ::popupRecipe)
        add("cookbook:select", ::selectRecipe)
        add("cookbook:sidebar", ::sideBarRecipe)
        add("cookbook:snackbar", ::snackbarRecipe)
        add("cookbook:splitPane", ::splitPaneRecipe)
        add("cookbook:svg", ::svgRecipe)
        add("cookbook:text", ::textRecipe)
        add("cookbook:tree", ::treeRecipe)
    }
}