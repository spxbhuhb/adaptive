package `fun`.adaptive.sandbox

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.sandbox.recipe.demo.goodmorning.goodMorning
import `fun`.adaptive.sandbox.recipe.demo.markdown.markdownDemoRecipe
import `fun`.adaptive.sandbox.recipe.document.document.documentInlineExample
import `fun`.adaptive.sandbox.recipe.document.document.documentRemoteExample
import `fun`.adaptive.sandbox.recipe.document.markdown.actualizedFromMarkdownExample
import `fun`.adaptive.sandbox.recipe.document.document.documentResourceExample
import `fun`.adaptive.sandbox.recipe.document.markdown.markdownInlineExample
import `fun`.adaptive.sandbox.recipe.document.markdown.markdownActualizeExample
import `fun`.adaptive.sandbox.recipe.document.markdown.markdownHintExample
import `fun`.adaptive.sandbox.recipe.document.markdown.markdownStyledExample
import `fun`.adaptive.sandbox.recipe.document.xlsx.xlsxDownloadExample
import `fun`.adaptive.sandbox.recipe.ui.badge.*
import `fun`.adaptive.sandbox.recipe.ui.canvas.canvasBasicExample
import `fun`.adaptive.sandbox.recipe.ui.canvas.canvasPositionExample
import `fun`.adaptive.sandbox.recipe.ui.checkbox.checkboxRecipe
import `fun`.adaptive.sandbox.recipe.ui.codefence.codeFenceRecipe
import `fun`.adaptive.sandbox.recipe.ui.container.containerPlayground
import `fun`.adaptive.sandbox.recipe.ui.editor.double_.doubleEditorBasic
import `fun`.adaptive.sandbox.recipe.ui.editor.double_.doubleEditorConfig
import `fun`.adaptive.sandbox.recipe.ui.event.eventRecipe
import `fun`.adaptive.sandbox.recipe.ui.filter.quickFilterRecipe
import `fun`.adaptive.sandbox.recipe.ui.form.formBasicExample
import `fun`.adaptive.sandbox.recipe.ui.icon.iconRecipe
import `fun`.adaptive.sandbox.recipe.ui.input.badge.badgeBasicInput
import `fun`.adaptive.sandbox.recipe.ui.input.badge.badgeInputInitializedNoLabel
import `fun`.adaptive.sandbox.recipe.ui.input.badge.badgeInputInitializedUnremovable
import `fun`.adaptive.sandbox.recipe.ui.input.button.*
import `fun`.adaptive.sandbox.recipe.ui.input.color.colorInputExample
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
import `fun`.adaptive.sandbox.recipe.ui.menu.menuBasic
import `fun`.adaptive.sandbox.recipe.ui.menu.menuContext
import `fun`.adaptive.sandbox.recipe.ui.menu.menuItemActions
import `fun`.adaptive.sandbox.recipe.ui.menu.menuPrimary
import `fun`.adaptive.sandbox.recipe.ui.mpw.mpwAllPanesExample
import `fun`.adaptive.sandbox.recipe.ui.mpw.mpwDoubleShiftExample
import `fun`.adaptive.sandbox.recipe.ui.popup.modal.modalPopupCustom
import `fun`.adaptive.sandbox.recipe.ui.popup.modal.modalPopupMultiPage
import `fun`.adaptive.sandbox.recipe.ui.popup.modal.modalPopupStandard
import `fun`.adaptive.sandbox.recipe.ui.popup.popupRecipe
import `fun`.adaptive.sandbox.recipe.ui.sidebar.sideBarRecipe
import `fun`.adaptive.sandbox.recipe.ui.snackbar.snackbarRecipe
import `fun`.adaptive.sandbox.recipe.ui.svg.svgRecipe
import `fun`.adaptive.sandbox.recipe.ui.tab.tabRecipe
import `fun`.adaptive.sandbox.recipe.ui.text.paragraphBasicExample
import `fun`.adaptive.sandbox.recipe.ui.text.paragraphLongExample
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
        add("cookbook:recipe:checkbox", ::checkboxRecipe)
        add("cookbook:recipe:codefence", ::codeFenceRecipe)
        add("cookbook:recipe:event", ::eventRecipe)
        add("cookbook:recipe:grid", ::gridRecipe)
        add("cookbook:recipe:icon", ::iconRecipe)
        add("cookbook:recipe:markdown:demo", ::markdownDemoRecipe)
        add("cookbook:recipe:popup", ::popupRecipe)
        add("cookbook:recipe:sidebar", ::sideBarRecipe)
        add("cookbook:recipe:snackbar", ::snackbarRecipe)
        add("cookbook:recipe:svg", ::svgRecipe)
        add("cookbook:recipe:tab", ::tabRecipe)
        add("cookbook:recipe:text", ::textRecipe)

        add("cookbook:recipe:input:quick-filter", ::quickFilterRecipe)

        // ----------------------------------------------------------------------------
        // Canvas
        // ----------------------------------------------------------------------------

        add("canvasBasicExample", ::canvasBasicExample)
        add("canvasPositionExample", ::canvasPositionExample)

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
        // Color input
        // ----------------------------------------------------------------------------

        add("colorInputExample", ::colorInputExample)

        // ----------------------------------------------------------------------------
        // Document
        // ----------------------------------------------------------------------------

        add("documentResourceExample", ::documentResourceExample)
        add("documentRemoteExample", ::documentRemoteExample)
        add("documentInlineExample", ::documentInlineExample)

        // ----------------------------------------------------------------------------
        // Markdown
        // ----------------------------------------------------------------------------

        add("markdownInlineExample", ::markdownInlineExample)
        add("markdownHintExample", ::markdownHintExample)
        add("markdownStyledExample", ::markdownStyledExample)
        add("markdownActualizeExample", ::markdownActualizeExample)
        add("cookbook-actualized-example", ::actualizedFromMarkdownExample)

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
        // Double
        // ----------------------------------------------------------------------------

        add("doubleInputDefault", ::doubleInputDefault)
        add("doubleInput1decNullable", ::doubleInput1decNullable)
        add("doubleInput8dec", ::doubleInput8dec)
        add("doubleInputNoDec", ::doubleInputNoDec)
        add("doubleInputUnit", ::doubleInputUnit)
        add("doubleInputOnChange", ::doubleInputOnChange)

        add("doubleEditorBasic", ::doubleEditorBasic)
        add("doubleEditorConfig", ::doubleEditorConfig)

        // ----------------------------------------------------------------------------
        // Int Input
        // ----------------------------------------------------------------------------

        add("intInputBasicExample", ::intInputBasicExample)
        add("intInputNullableExample", ::intInputNullableExample)
        add("intInputRadixExample", ::intInputRadixExample)
        add("intInputRadixWithDecimalExample", ::intInputRadixWithDecimalExample)
        add("intInputUnitExample", ::intInputUnitExample)

        // ----------------------------------------------------------------------------
        // MPW
        // ----------------------------------------------------------------------------

        add("mpwAllPanesExample", ::mpwAllPanesExample)
        add("mpwDoubleShiftExample", ::mpwDoubleShiftExample)

        // ----------------------------------------------------------------------------
        // Paragraph
        // ----------------------------------------------------------------------------

        add("paragraphBasicExample", ::paragraphBasicExample)
        add("paragraphLongExample", ::paragraphLongExample)

        // ----------------------------------------------------------------------------
        // Select Input
        // ----------------------------------------------------------------------------

        add("selectInputDropdownExample", ::selectInputDropdownExample)
        add("selectInputTextExample", ::selectInputTextExample)
        add("selectInputIconAndTextExample", ::selectInputIconAndTextExample)
        add("selectInputCheckboxExample", ::selectInputCheckboxExample)

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

        add("treeBasicExample", ::treeBasicExample)
        add("treeValueExample", ::treeValueExample)

        add("cookbook/tree/playground", ::treePlayground)

        // ----------------------------------------------------------------------------
        // Xlsx
        // ----------------------------------------------------------------------------

        add("xlsxDownloadExample", ::xlsxDownloadExample)


    }
}