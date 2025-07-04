package `fun`.adaptive.sandbox

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.sandbox.recipe.demo.goodmorning.goodMorning
import `fun`.adaptive.sandbox.recipe.demo.markdown.markdownDemoRecipe
import `fun`.adaptive.sandbox.recipe.ui.badge.*
import `fun`.adaptive.sandbox.recipe.ui.canvas.canvasRecipe
import `fun`.adaptive.sandbox.recipe.ui.checkbox.checkboxRecipe
import `fun`.adaptive.sandbox.recipe.ui.codefence.codeFenceRecipe
import `fun`.adaptive.sandbox.recipe.ui.container.containerPlayground
import `fun`.adaptive.sandbox.recipe.ui.event.eventRecipe
import `fun`.adaptive.sandbox.recipe.ui.filter.quickFilterRecipe
import `fun`.adaptive.sandbox.recipe.ui.form.formBasicExample
import `fun`.adaptive.sandbox.recipe.ui.icon.iconRecipe
import `fun`.adaptive.sandbox.recipe.ui.input.badge.badgeBasicInput
import `fun`.adaptive.sandbox.recipe.ui.input.badge.badgeInputInitializedNoLabel
import `fun`.adaptive.sandbox.recipe.ui.input.badge.badgeInputInitializedUnremovable
import `fun`.adaptive.sandbox.recipe.ui.input.button.*
import `fun`.adaptive.sandbox.recipe.ui.input.date.dateInputExample
import `fun`.adaptive.sandbox.recipe.ui.input.datetime.dateTimeInputExample
import `fun`.adaptive.sandbox.recipe.ui.input.double_.*
import `fun`.adaptive.sandbox.recipe.ui.input.integer.*
import `fun`.adaptive.sandbox.recipe.ui.input.select.*
import `fun`.adaptive.sandbox.recipe.ui.input.text.textInputAreaExample
import `fun`.adaptive.sandbox.recipe.ui.input.text.textInputPlayground
import `fun`.adaptive.sandbox.recipe.ui.input.text.textInputSimpleExample
import `fun`.adaptive.sandbox.recipe.ui.input.time.timeInputExample
import `fun`.adaptive.sandbox.recipe.ui.input.timerange.timeRangeInputExample
import `fun`.adaptive.sandbox.recipe.ui.layout.box.boxRecipe
import `fun`.adaptive.sandbox.recipe.ui.layout.grid.gridRecipe
import `fun`.adaptive.sandbox.recipe.ui.layout.splitpane.splitPaneProportionalExample
import `fun`.adaptive.sandbox.recipe.ui.layout.splitpane.splitPaneWrapperExample
import `fun`.adaptive.sandbox.recipe.ui.layout.workspace.workspaceRecipe
import `fun`.adaptive.sandbox.recipe.ui.menu.menuBasic
import `fun`.adaptive.sandbox.recipe.ui.menu.menuContext
import `fun`.adaptive.sandbox.recipe.ui.menu.menuItemActions
import `fun`.adaptive.sandbox.recipe.ui.menu.menuPrimary
import `fun`.adaptive.sandbox.recipe.ui.popup.modal.modalPopupCustom
import `fun`.adaptive.sandbox.recipe.ui.popup.modal.modalPopupMultiPage
import `fun`.adaptive.sandbox.recipe.ui.popup.modal.modalPopupStandard
import `fun`.adaptive.sandbox.recipe.ui.popup.popupRecipe
import `fun`.adaptive.sandbox.recipe.ui.sidebar.sideBarRecipe
import `fun`.adaptive.sandbox.recipe.ui.snackbar.snackbarRecipe
import `fun`.adaptive.sandbox.recipe.ui.svg.svgRecipe
import `fun`.adaptive.sandbox.recipe.ui.tab.tabRecipe
import `fun`.adaptive.sandbox.recipe.ui.text.paragraphRecipe
import `fun`.adaptive.sandbox.recipe.ui.text.textRecipe
import `fun`.adaptive.sandbox.recipe.ui.tree.treeBasicExample
import `fun`.adaptive.sandbox.recipe.ui.tree.treePlayground
import `fun`.adaptive.sandbox.recipe.ui.tree.treeValueExample
import `fun`.adaptive.sandbox.support.exampleGroup

object CookbookFragmentFactory : FoundationFragmentFactory() {
    init {
        add("example-group", ::exampleGroup)

        //add("cookbook:recipe:auth", ::authRecipe)
        add("cookbook:recipe:goodmorning", ::goodMorning)

        add("cookbook:recipe:box", ::boxRecipe)
        add("cookbook:recipe:canvas", ::canvasRecipe)
        add("cookbook:recipe:checkbox", ::checkboxRecipe)
        add("cookbook:recipe:codefence", ::codeFenceRecipe)
        add("cookbook:recipe:event", ::eventRecipe)
        add("cookbook:recipe:grid", ::gridRecipe)
        add("cookbook:recipe:icon", ::iconRecipe)
        add("cookbook:recipe:markdown:demo", ::markdownDemoRecipe)
        add("cookbook:recipe:paragraph", ::paragraphRecipe)
        add("cookbook:recipe:popup", ::popupRecipe)
        add("cookbook:recipe:sidebar", ::sideBarRecipe)
        add("cookbook:recipe:snackbar", ::snackbarRecipe)
        add("cookbook:recipe:svg", ::svgRecipe)
        add("cookbook:recipe:tab", ::tabRecipe)
        add("cookbook:recipe:text", ::textRecipe)
        add("cookbook:recipe:workspace", ::workspaceRecipe)

        add("cookbook:recipe:input:quick-filter", ::quickFilterRecipe)

        // ----------------------------------------------------------------------------
        // Container
        // ----------------------------------------------------------------------------

        add("cookbook/container/playground", ::containerPlayground)
        add("cookbook/example/split-pane-proportional", ::splitPaneProportionalExample)
        add("cookbook/example/split-pane-wrapper", ::splitPaneWrapperExample)

        // ----------------------------------------------------------------------------
        // Form
        // ----------------------------------------------------------------------------

        add("formBasicExample", ::formBasicExample)

        // ----------------------------------------------------------------------------
        // Badge
        // ----------------------------------------------------------------------------

        add("badgeNameOnly", ::badgeNameOnly)
        add("badgeNameAndRemovable", ::badgeNameAndRemovable)
        add("badgeNameAndIcon", ::badgeNameAndIcon)
        add("badgeNameIconAndRemovable", ::badgeNameIconAndRemovable)
        add("badgeWithSeverity", ::badgeWithSeverity)
        add("badgeCustomStyles", ::badgeCustomStyles)
        add("badgeValueBadge", ::badgeValueBadge)
        add("badgeContentPaneHeader", ::badgeContentPaneHeader)
        add("badgeBasicInput", ::badgeBasicInput)
        add("badgeInputInitializedNoLabel", ::badgeInputInitializedNoLabel)
        add("badgeInputInitializedUnremovable", ::badgeInputInitializedUnremovable)

        // ----------------------------------------------------------------------------
        // Menu
        // ----------------------------------------------------------------------------

        add("menuBasic", ::menuBasic)
        add("menuPrimary", ::menuPrimary)
        add("menuContext", ::menuContext)
        add("menuItemActions", ::menuItemActions)

        // ----------------------------------------------------------------------------
        // Popup modal
        // ----------------------------------------------------------------------------

        add("modalPopupStandard", ::modalPopupStandard)
        add("modalPopupCustom", ::modalPopupCustom)
        add("modalPopupMultiPage", ::modalPopupMultiPage)

        // ----------------------------------------------------------------------------
        // Button
        // ----------------------------------------------------------------------------

        add("buttonBasicExample", ::buttonBasicExample)
        add("buttonDisabledExample", ::buttonDisabledExample)
        add("buttonSubmitExample", ::buttonSubmitExample)
        add("buttonDangerExample", ::buttonDangerExample)

        add("cookbook/input/button/playground", ::buttonPlayground)

        // ----------------------------------------------------------------------------
        // Date Input
        // ----------------------------------------------------------------------------

        add("dateTimeInputExample", ::dateTimeInputExample)

        // ----------------------------------------------------------------------------
        // Date Input
        // ----------------------------------------------------------------------------

        add("dateInputExample", ::dateInputExample)

        // ----------------------------------------------------------------------------
        // Double Input
        // ----------------------------------------------------------------------------

        add("doubleInputDefault", ::doubleInputDefault)
        add("doubleInput1decNullable", ::doubleInput1decNullable)
        add("doubleInput8dec", ::doubleInput8dec)
        add("doubleInputNoDec", ::doubleInputNoDec)
        add("doubleInputUnit", ::doubleInputUnit)
        add("doubleInputOnChange", ::doubleInputOnChange)

        // ----------------------------------------------------------------------------
        // Int Input
        // ----------------------------------------------------------------------------

        add("intInputBasicExample", ::intInputBasicExample)
        add("intInputNullableExample", ::intInputNullableExample)
        add("intInputRadixExample", ::intInputRadixExample)
        add("intInputRadixWithDecimalExample", ::intInputRadixWithDecimalExample)
        add("intInputUnitExample", ::intInputUnitExample)

        // ----------------------------------------------------------------------------
        // Select Input
        // ----------------------------------------------------------------------------

        add("cookbook/input/select/example/dropdown", ::selectInputDropdownExample)
        add("cookbook/input/select/example/text", ::selectInputTextExample)
        add("cookbook/input/select/example/icon-and-text", ::selectInputIconAndTextExample)
        add("cookbook/input/select/example/checkbox", ::selectInputCheckboxExample)

        add("cookbook/input/select/playground", ::selectInputPlayground)

        // ----------------------------------------------------------------------------
        // Text Input
        // ----------------------------------------------------------------------------

        add("textInputSimpleExample", ::textInputSimpleExample)
        add("textInputAreaExample", ::textInputAreaExample)

        add("cookbook/input/text/playground", ::textInputPlayground)

        // ----------------------------------------------------------------------------
        // Time Input
        // ----------------------------------------------------------------------------

        add("timeInputExample", ::timeInputExample)

        // ----------------------------------------------------------------------------
        // TimeRange Input
        // ----------------------------------------------------------------------------

        add("timeRangeInputExample", ::timeRangeInputExample)

        // ----------------------------------------------------------------------------
        // Tree
        // ----------------------------------------------------------------------------

        add("cookbook/tree/example/basic", ::treeBasicExample)
        add("cookbook/tree/example/value", ::treeValueExample)

        add("cookbook/tree/playground", ::treePlayground)

    }
}