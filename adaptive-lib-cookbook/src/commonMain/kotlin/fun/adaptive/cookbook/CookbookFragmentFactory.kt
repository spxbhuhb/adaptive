package `fun`.adaptive.cookbook

import `fun`.adaptive.cookbook.recipe.demo.goodmorning.goodMorning
import `fun`.adaptive.cookbook.recipe.ui.button.buttonRecipe
import `fun`.adaptive.cookbook.recipe.ui.canvas.canvasRecipe
import `fun`.adaptive.cookbook.recipe.ui.checkbox.checkboxRecipe
import `fun`.adaptive.cookbook.recipe.ui.codefence.codeFenceRecipe
import `fun`.adaptive.cookbook.recipe.ui.datetime.datePickerRecipe
import `fun`.adaptive.cookbook.recipe.ui.dialog.dialogRecipe
import `fun`.adaptive.cookbook.recipe.ui.editor.editorRecipe
import `fun`.adaptive.cookbook.recipe.ui.event.eventRecipe
import `fun`.adaptive.cookbook.recipe.ui.form.formRecipe
import `fun`.adaptive.cookbook.recipe.ui.icon.iconRecipe
import `fun`.adaptive.cookbook.recipe.ui.layout.box.boxRecipe
import `fun`.adaptive.cookbook.recipe.ui.layout.grid.gridRecipe
import `fun`.adaptive.cookbook.recipe.ui.popup.popupRecipe
import `fun`.adaptive.cookbook.recipe.ui.select.selectRecipe
import `fun`.adaptive.cookbook.recipe.ui.sidebar.sideBarRecipe
import `fun`.adaptive.cookbook.recipe.ui.snackbar.snackbarRecipe
import `fun`.adaptive.cookbook.recipe.ui.splitpane.splitPaneRecipe
import `fun`.adaptive.cookbook.recipe.ui.svg.svgRecipe
import `fun`.adaptive.cookbook.recipe.ui.tab.tabRecipe
import `fun`.adaptive.cookbook.recipe.ui.text.paragraphRecipe
import `fun`.adaptive.cookbook.recipe.ui.text.textRecipe
import `fun`.adaptive.cookbook.recipe.ui.tree.treeRecipe
import `fun`.adaptive.cookbook.recipe.ui.workspace.workspaceRecipe
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object CookbookFragmentFactory : FoundationFragmentFactory() {
    init {
        add("cookbook:recipes", ::cookbookRecipes)
        add("cookbook:center", ::cookbookCenter)

        //add("cookbook:recipe:auth", ::authRecipe)
        add("cookbook:recipe:goodmorning", ::goodMorning)

        add("cookbook:recipe:box", ::boxRecipe)
        add("cookbook:recipe:button", ::buttonRecipe)
        add("cookbook:recipe:canvas", ::canvasRecipe)
        add("cookbook:recipe:checkbox", ::checkboxRecipe)
        add("cookbook:recipe:codefence", ::codeFenceRecipe)
        add("cookbook:recipe:datepicker", ::datePickerRecipe)
        add("cookbook:recipe:dialog", ::dialogRecipe)
        add("cookbook:recipe:editor", ::editorRecipe)
        add("cookbook:recipe:event", ::eventRecipe)
        add("cookbook:recipe:form", ::formRecipe)
        add("cookbook:recipe:grid", ::gridRecipe)
        add("cookbook:recipe:icon", ::iconRecipe)
        add("cookbook:recipe:paragraph", ::paragraphRecipe)
        add("cookbook:recipe:popup", ::popupRecipe)
        add("cookbook:recipe:select", ::selectRecipe)
        add("cookbook:recipe:sidebar", ::sideBarRecipe)
        add("cookbook:recipe:snackbar", ::snackbarRecipe)
        add("cookbook:recipe:splitpane", ::splitPaneRecipe)
        add("cookbook:recipe:svg", ::svgRecipe)
        add("cookbook:recipe:tab", ::tabRecipe)
        add("cookbook:recipe:text", ::textRecipe)
        add("cookbook:recipe:tree", ::treeRecipe)
        add("cookbook:recipe:workspace", ::workspaceRecipe)
    }
}