package `fun`.adaptive.sandbox

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.sandbox.recipe.demo.goodmorning.goodMorning
import `fun`.adaptive.sandbox.recipe.demo.markdown.markdownDemoRecipe
import `fun`.adaptive.sandbox.recipe.ui.canvas.canvasRecipe
import `fun`.adaptive.sandbox.recipe.ui.checkbox.checkboxRecipe
import `fun`.adaptive.sandbox.recipe.ui.codefence.codeFenceRecipe
import `fun`.adaptive.sandbox.recipe.ui.dialog.dialogRecipe
import `fun`.adaptive.sandbox.recipe.ui.event.eventRecipe
import `fun`.adaptive.sandbox.recipe.ui.filter.quickFilterRecipe
import `fun`.adaptive.sandbox.recipe.ui.form.formBasicExample
import `fun`.adaptive.sandbox.recipe.ui.form.formRecipe
import `fun`.adaptive.sandbox.recipe.ui.icon.iconRecipe
import `fun`.adaptive.sandbox.recipe.ui.input.button.buttonDangerExample
import `fun`.adaptive.sandbox.recipe.ui.input.button.buttonDisabledExample
import `fun`.adaptive.sandbox.recipe.ui.input.button.buttonNormalExample
import `fun`.adaptive.sandbox.recipe.ui.input.button.buttonPlayground
import `fun`.adaptive.sandbox.recipe.ui.input.button.buttonRecipe
import `fun`.adaptive.sandbox.recipe.ui.input.button.buttonSubmitExample
import `fun`.adaptive.sandbox.recipe.ui.input.datetime.dateInputRecipe
import `fun`.adaptive.sandbox.recipe.ui.input.number.doubleInputRecipe
import `fun`.adaptive.sandbox.recipe.ui.input.select.*
import `fun`.adaptive.sandbox.recipe.ui.input.text.textInputAreaRecipe
import `fun`.adaptive.sandbox.recipe.ui.input.text.textInputPlayground
import `fun`.adaptive.sandbox.recipe.ui.input.text.textInputRecipe
import `fun`.adaptive.sandbox.recipe.ui.input.text.textInputSimpleExample
import `fun`.adaptive.sandbox.recipe.ui.layout.box.boxRecipe
import `fun`.adaptive.sandbox.recipe.ui.layout.grid.gridRecipe
import `fun`.adaptive.sandbox.recipe.ui.layout.splitpane.splitPaneProportionalExample
import `fun`.adaptive.sandbox.recipe.ui.layout.splitpane.splitPaneRecipe
import `fun`.adaptive.sandbox.recipe.ui.layout.splitpane.splitPaneWrapperExample
import `fun`.adaptive.sandbox.recipe.ui.layout.workspace.workspaceRecipe
import `fun`.adaptive.sandbox.recipe.ui.popup.popupRecipe
import `fun`.adaptive.sandbox.recipe.ui.sidebar.sideBarRecipe
import `fun`.adaptive.sandbox.recipe.ui.snackbar.snackbarRecipe
import `fun`.adaptive.sandbox.recipe.ui.svg.svgRecipe
import `fun`.adaptive.sandbox.recipe.ui.tab.tabRecipe
import `fun`.adaptive.sandbox.recipe.ui.text.paragraphRecipe
import `fun`.adaptive.sandbox.recipe.ui.text.textRecipe
import `fun`.adaptive.sandbox.recipe.ui.tree.treeBasicExample
import `fun`.adaptive.sandbox.recipe.ui.tree.treePlayground
import `fun`.adaptive.sandbox.recipe.ui.tree.treeRecipe
import `fun`.adaptive.sandbox.recipe.ui.tree.treeValueExample

object CookbookFragmentFactory : FoundationFragmentFactory() {
    init {
        //add("cookbook:recipe:auth", ::authRecipe)
        add("cookbook:recipe:goodmorning", ::goodMorning)

        add("cookbook:recipe:box", ::boxRecipe)
        add("cookbook:recipe:button", ::buttonRecipe)
        add("cookbook:recipe:canvas", ::canvasRecipe)
        add("cookbook:recipe:checkbox", ::checkboxRecipe)
        add("cookbook:recipe:codefence", ::codeFenceRecipe)
        add("cookbook:recipe:dialog", ::dialogRecipe)
        add("cookbook:recipe:event", ::eventRecipe)
        add("cookbook:recipe:form", ::formRecipe)
        add("cookbook:recipe:grid", ::gridRecipe)
        add("cookbook:recipe:icon", ::iconRecipe)
        add("cookbook:recipe:markdown:demo", ::markdownDemoRecipe)
        add("cookbook:recipe:paragraph", ::paragraphRecipe)
        add("cookbook:recipe:popup", ::popupRecipe)
        add("cookbook:recipe:sidebar", ::sideBarRecipe)
        add("cookbook:recipe:snackbar", ::snackbarRecipe)
        add("cookbook:recipe:splitpane", ::splitPaneRecipe)
        add("cookbook:recipe:svg", ::svgRecipe)
        add("cookbook:recipe:tab", ::tabRecipe)
        add("cookbook:recipe:text", ::textRecipe)
        add("cookbook:recipe:tree", ::treeRecipe)
        add("cookbook:recipe:workspace", ::workspaceRecipe)

        add("cookbook:recipe:input:date", ::dateInputRecipe)
        add("cookbook:recipe:input:double", ::doubleInputRecipe)
        add("cookbook:recipe:input:quick-filter", ::quickFilterRecipe)
        add("cookbook:recipe:input:text-area", ::textInputAreaRecipe)

        add("cookbook/example/split-pane-proportional", ::splitPaneProportionalExample)
        add("cookbook/example/split-pane-wrapper", ::splitPaneWrapperExample)

        // ----------------------------------------------------------------------------
        // Form
        // ----------------------------------------------------------------------------

        add("cookbook/form/example/basic", ::formBasicExample)

        // ----------------------------------------------------------------------------
        // Button
        // ----------------------------------------------------------------------------

        add("cookbook/input/button/example/normal", ::buttonNormalExample)
        add("cookbook/input/button/example/disabled", ::buttonDisabledExample)
        add("cookbook/input/button/example/submit", ::buttonSubmitExample)
        add("cookbook/input/button/example/danger", ::buttonDangerExample)

        add("cookbook/input/button/playground", ::buttonPlayground)
        add("cookbook/input/button/recipe", ::buttonRecipe)

        // ----------------------------------------------------------------------------
        // Select Input
        // ----------------------------------------------------------------------------

        add("cookbook/input/select/example/dropdown", ::selectInputDropdownExample)
        add("cookbook/input/select/example/text", ::selectInputTextExample)
        add("cookbook/input/select/example/icon-and-text", ::selectInputIconAndTextExample)
        add("cookbook/input/select/example/checkbox", ::selectInputCheckboxExample)

        add("cookbook/input/select/playground", ::selectInputPlayground)
        add("cookbook/input/select/recipe", ::selectInputRecipe)

        // ----------------------------------------------------------------------------
        // Text Input
        // ----------------------------------------------------------------------------

        add("cookbook/input/text/example/simple", ::textInputSimpleExample)

        add("cookbook/input/text/playground", ::textInputPlayground)
        add("cookbook/input/text/recipe", ::textInputRecipe)

        // ----------------------------------------------------------------------------
        // Tree
        // ----------------------------------------------------------------------------

        add("cookbook/tree/example/basic", ::treeBasicExample)
        add("cookbook/tree/example/value", ::treeValueExample)

        add("cookbook/tree/playground", ::treePlayground)
        add("cookbook/tree/recipe", ::treeRecipe)

    }
}