package `fun`.adaptive.doc.app

import `fun`.adaptive.doc.example.badge.badgeContentPaneHeader
import `fun`.adaptive.doc.example.badge.badgeCustomStyles
import `fun`.adaptive.doc.example.badge.badgeNameAndIcon
import `fun`.adaptive.doc.example.badge.badgeNameAndRemovable
import `fun`.adaptive.doc.example.badge.badgeNameIconAndRemovable
import `fun`.adaptive.doc.example.badge.badgeNameOnly
import `fun`.adaptive.doc.example.badge.badgeValueBadge
import `fun`.adaptive.doc.example.badge.badgeWithSeverity
import `fun`.adaptive.doc.example.badgeInput.badgeBasicInput
import `fun`.adaptive.doc.example.badgeInput.badgeInputInitializedNoLabel
import `fun`.adaptive.doc.example.badgeInput.badgeInputInitializedUnremovable
import `fun`.adaptive.doc.example.booleanInput.booleanInputBasicExample
import `fun`.adaptive.doc.example.booleanInput.booleanInputLabelAlignmentExample
import `fun`.adaptive.doc.example.booleanInput.booleanInputNullableExample
import `fun`.adaptive.doc.example.booleanInput.booleanInputThemeSmallExample
import `fun`.adaptive.doc.example.button.buttonBasicExample
import `fun`.adaptive.doc.example.button.buttonDangerExample
import `fun`.adaptive.doc.example.button.buttonDisabledExample
import `fun`.adaptive.doc.example.button.buttonPlayground
import `fun`.adaptive.doc.example.button.buttonSubmitExample
import `fun`.adaptive.doc.example.canvas.canvasBasicAnimationExample
import `fun`.adaptive.doc.example.canvas.canvasLineExample
import `fun`.adaptive.doc.example.canvas.canvasMeterExample
import `fun`.adaptive.doc.example.canvas.canvasPathArcExample
import `fun`.adaptive.doc.example.canvas.canvasPathCubicCurveExample
import `fun`.adaptive.doc.example.canvas.canvasPathLineToExample
import `fun`.adaptive.doc.example.canvas.canvasPathQuadraticCurveExample
import `fun`.adaptive.doc.example.canvas.canvasPositionExample
import `fun`.adaptive.doc.example.canvas.canvasTextPositionExample
import `fun`.adaptive.doc.example.chart.chartBasicExample
import `fun`.adaptive.doc.example.codefence.codeFenceScrollingExample
import `fun`.adaptive.doc.example.codefence.codeFenceSmallExample
import `fun`.adaptive.doc.example.colorInput.colorInputExample
import `fun`.adaptive.doc.example.container.containerPlayground
import `fun`.adaptive.doc.example.dateInput.dateInputExample
import `fun`.adaptive.doc.example.dateTimeInput.dateTimeInputExample
import `fun`.adaptive.doc.example.document.documentInlineExample
import `fun`.adaptive.doc.example.document.documentRemoteExample
import `fun`.adaptive.doc.example.document.documentResourceExample
import `fun`.adaptive.doc.example.doubleEditor.doubleEditorBasic
import `fun`.adaptive.doc.example.doubleEditor.doubleEditorConfigExample
import `fun`.adaptive.doc.example.doubleInput.doubleInput1decNullable
import `fun`.adaptive.doc.example.doubleInput.doubleInput8dec
import `fun`.adaptive.doc.example.doubleInput.doubleInputDefault
import `fun`.adaptive.doc.example.doubleInput.doubleInputNoDec
import `fun`.adaptive.doc.example.doubleInput.doubleInputOnChange
import `fun`.adaptive.doc.example.doubleInput.doubleInputUnit
import `fun`.adaptive.doc.example.durationEditor.durationEditorBasicExample
import `fun`.adaptive.doc.example.durationInput.durationInputBasic
import `fun`.adaptive.doc.example.durationInput.durationInputNullable
import `fun`.adaptive.doc.example.enumEditor.enumEditorDropdownExample
import `fun`.adaptive.doc.example.enumEditor.enumEditorListExample
import `fun`.adaptive.doc.example.form.formBasicExample
import `fun`.adaptive.doc.example.handle.handleBasicExample
import `fun`.adaptive.doc.example.handle.handleBoxedExample
import `fun`.adaptive.doc.example.handle.handleHorizonalOnlyExample
import `fun`.adaptive.doc.example.icon.iconActionExample
import `fun`.adaptive.doc.example.icon.iconDefaultExample
import `fun`.adaptive.doc.example.icon.iconDenseExample
import `fun`.adaptive.doc.example.icon.iconPrimaryExample
import `fun`.adaptive.doc.example.icon.iconTableExample
import `fun`.adaptive.doc.example.intInput.intInputBasicExample
import `fun`.adaptive.doc.example.intInput.intInputNullableExample
import `fun`.adaptive.doc.example.intInput.intInputRadixExample
import `fun`.adaptive.doc.example.intInput.intInputRadixWithDecimalExample
import `fun`.adaptive.doc.example.intInput.intInputUnitExample
import `fun`.adaptive.doc.example.layout.boxwithproposal.boxWithProposalBasicExample
import `fun`.adaptive.doc.example.layout.splitpane.splitPaneProportionalExample
import `fun`.adaptive.doc.example.layout.splitpane.splitPaneWrapperExample
import `fun`.adaptive.doc.example.markdown.actualizedFromMarkdownExample
import `fun`.adaptive.doc.example.markdown.markdownActualizeExample
import `fun`.adaptive.doc.example.markdown.markdownCompleteExample
import `fun`.adaptive.doc.example.markdown.markdownCompleteHintExample
import `fun`.adaptive.doc.example.markdown.markdownHintExample
import `fun`.adaptive.doc.example.markdown.markdownInlineExample
import `fun`.adaptive.doc.example.markdown.markdownStyledExample
import `fun`.adaptive.doc.example.menu.menuBasic
import `fun`.adaptive.doc.example.menu.menuContext
import `fun`.adaptive.doc.example.menu.menuItemActions
import `fun`.adaptive.doc.example.menu.menuPrimary
import `fun`.adaptive.doc.example.mpw.WorkspaceRecipePaneFragmentFactory
import `fun`.adaptive.doc.example.mpw.mpwAllPanesExample
import `fun`.adaptive.doc.example.mpw.mpwDoubleShiftExample
import `fun`.adaptive.doc.example.mpw.mpwToolPaneExampleWrapper
import `fun`.adaptive.doc.example.paragraph.paragraphBasicExample
import `fun`.adaptive.doc.example.paragraph.paragraphLongExample
import `fun`.adaptive.doc.example.paragraph.paragraphNavigatorExample
import `fun`.adaptive.doc.example.popup.modal.modalPopupCustom
import `fun`.adaptive.doc.example.popup.modal.modalPopupMultiPage
import `fun`.adaptive.doc.example.popup.modal.modalPopupStandard
import `fun`.adaptive.doc.example.popup.popupPlayground
import `fun`.adaptive.doc.example.producer.focusBasicExample
import `fun`.adaptive.doc.example.producer.hoverBasicExample
import `fun`.adaptive.doc.example.producer.mediaMetricsBasicExample
import `fun`.adaptive.doc.example.selectEditor.selectEditorMappingExample
import `fun`.adaptive.doc.example.selectEditor.selectEditorRefManualExample
import `fun`.adaptive.doc.example.selectEditor.selectEditorRefMultiExample
import `fun`.adaptive.doc.example.selectEditor.selectEditorRefNameExample
import `fun`.adaptive.doc.example.selectEditor.selectEditorRefPathExample
import `fun`.adaptive.doc.example.selectInput.selectInputCheckboxExample
import `fun`.adaptive.doc.example.selectInput.selectInputCustomExample
import `fun`.adaptive.doc.example.selectInput.selectInputDropdownExample
import `fun`.adaptive.doc.example.selectInput.selectInputIconAndTextExample
import `fun`.adaptive.doc.example.selectInput.selectInputPlayground
import `fun`.adaptive.doc.example.selectInput.selectInputTextExample
import `fun`.adaptive.doc.example.statusEditor.statusEditorSingleExample
import `fun`.adaptive.doc.example.statusInput.statusInputSingleExample
import `fun`.adaptive.doc.example.textInput.textInputAreaExample
import `fun`.adaptive.doc.example.textInput.textInputPlayground
import `fun`.adaptive.doc.example.textInput.textInputSimpleExample
import `fun`.adaptive.doc.example.timeInput.timeInputExample
import `fun`.adaptive.doc.example.timeRangeInput.timeRangeInputExample
import `fun`.adaptive.doc.example.tree.treeBasicExample
import `fun`.adaptive.doc.example.tree.treePlayground
import `fun`.adaptive.doc.example.tree.treeValueExample
import `fun`.adaptive.doc.example.value.valueViewBackendTreeExample
import `fun`.adaptive.doc.example.xlsx.xlsxDownloadExample
import `fun`.adaptive.doc.support.exampleGroup
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object ExampleFragmentFactory : FoundationFragmentFactory() {
    init {
        add("example-group", ::exampleGroup)

//        //add("example:recipe:auth", ::authRecipe)
//        add("example:recipe:goodmorning", ::goodMorning)
//
//        add("example:recipe:box", ::boxRecipe)
//        add("example:recipe:event", ::eventRecipe)
//        add("example:recipe:grid", ::gridRecipe)
//        add("example:recipe:sidebar", ::sideBarRecipe)
//        add("example:recipe:snackbar", ::snackbarRecipe)
//        add("example:recipe:svg", ::svgRecipe)
//        add("example:recipe:tab", ::tabRecipe)
//        add("example:recipe:text", ::textRecipe)
//
//        add("example:recipe:input:quick-filter", ::quickFilterRecipe)

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
        // Box with proposal
        // ----------------------------------------------------------------------------

        add("boxWithProposalBasicExample", ::boxWithProposalBasicExample)

        // ----------------------------------------------------------------------------
        // Button
        // ----------------------------------------------------------------------------

        add("buttonBasicExample", ::buttonBasicExample)
        add("buttonDisabledExample", ::buttonDisabledExample)
        add("buttonSubmitExample", ::buttonSubmitExample)
        add("buttonDangerExample", ::buttonDangerExample)

        add("example/input/button/playground", ::buttonPlayground)

        // ----------------------------------------------------------------------------
        // Canvas
        // ----------------------------------------------------------------------------

        add("canvasLineExample", ::canvasLineExample)
        add("canvasBasicAnimationExample", ::canvasBasicAnimationExample)
        add("canvasPositionExample", ::canvasPositionExample)
        add("canvasMeterExample", ::canvasMeterExample)
        add("canvasPathLineToExample", ::canvasPathLineToExample)
        add("canvasPathArcExample", ::canvasPathArcExample)
        add("canvasPathCubicCurveExample", ::canvasPathCubicCurveExample)
        add("canvasPathQuadraticCurveExample", ::canvasPathQuadraticCurveExample)
        add("canvasTextPositionExample", ::canvasTextPositionExample)

        // ----------------------------------------------------------------------------
        // Chart
        // ----------------------------------------------------------------------------

        add("chartBasicExample", ::chartBasicExample)

        // ----------------------------------------------------------------------------
        // Code fence
        // ----------------------------------------------------------------------------

        add("codeFenceSmallExample", ::codeFenceSmallExample)
        add("codeFenceScrollingExample", ::codeFenceScrollingExample)

        // ----------------------------------------------------------------------------
        // Color input
        // ----------------------------------------------------------------------------

        add("colorInputExample", ::colorInputExample)

        // ----------------------------------------------------------------------------
        // Container
        // ----------------------------------------------------------------------------

        add("example/example/split-pane-proportional", ::splitPaneProportionalExample)
        add("example/example/split-pane-wrapper", ::splitPaneWrapperExample)

        add("example/container/playground", ::containerPlayground)

        // ----------------------------------------------------------------------------
        // Date Input
        // ----------------------------------------------------------------------------

        add("dateTimeInputExample", ::dateTimeInputExample)

        // ----------------------------------------------------------------------------
        // Date Input
        // ----------------------------------------------------------------------------

        add("dateInputExample", ::dateInputExample)

        // ----------------------------------------------------------------------------
        // Document
        // ----------------------------------------------------------------------------

        add("documentResourceExample", ::documentResourceExample)
        add("documentRemoteExample", ::documentRemoteExample)
        add("documentInlineExample", ::documentInlineExample)

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
        add("doubleEditorConfigExample", ::doubleEditorConfigExample)

        // ----------------------------------------------------------------------------
        // Duration
        // ----------------------------------------------------------------------------

        add("durationInputBasic", ::durationInputBasic)
        add("durationInputNullable", ::durationInputNullable)

        add("durationEditorBasicExample", ::durationEditorBasicExample)

        // ----------------------------------------------------------------------------
        // Enum
        // ----------------------------------------------------------------------------

        add("enumEditorDropdownExample", ::enumEditorDropdownExample)
        add("enumEditorListExample", ::enumEditorListExample)

        // ----------------------------------------------------------------------------
        // Form
        // ----------------------------------------------------------------------------

        add("formBasicExample", ::formBasicExample)

        // ----------------------------------------------------------------------------
        // Handle
        // ----------------------------------------------------------------------------

        add("handleBasicExample", ::handleBasicExample)
        add("handleBoxedExample", ::handleBoxedExample)
        add("handleHorizonalOnlyExample", ::handleHorizonalOnlyExample)

        // ----------------------------------------------------------------------------
        // Icon
        // ----------------------------------------------------------------------------

        add("iconDefaultExample", ::iconDefaultExample)
        add("iconActionExample", ::iconActionExample)
        add("iconPrimaryExample", ::iconPrimaryExample)
        add("iconTableExample", ::iconTableExample)
        add("iconDenseExample", ::iconDenseExample)

        // ----------------------------------------------------------------------------
        // Int Input
        // ----------------------------------------------------------------------------

        add("intInputBasicExample", ::intInputBasicExample)
        add("intInputNullableExample", ::intInputNullableExample)
        add("intInputRadixExample", ::intInputRadixExample)
        add("intInputRadixWithDecimalExample", ::intInputRadixWithDecimalExample)
        add("intInputUnitExample", ::intInputUnitExample)

        // ----------------------------------------------------------------------------
        // Boolean Input
        // ----------------------------------------------------------------------------

        add("booleanInputBasicExample", ::booleanInputBasicExample)
        add("booleanInputNullableExample", ::booleanInputNullableExample)
        add("booleanInputLabelAlignmentExample", ::booleanInputLabelAlignmentExample)
        add("booleanInputThemeSmallExample", ::booleanInputThemeSmallExample)

        // ----------------------------------------------------------------------------
        // Markdown
        // ----------------------------------------------------------------------------

        add("markdownInlineExample", ::markdownInlineExample)
        add("markdownHintExample", ::markdownHintExample)
        add("markdownStyledExample", ::markdownStyledExample)
        add("markdownActualizeExample", ::markdownActualizeExample)
        add("markdownCompleteExample", ::markdownCompleteExample)
        add("markdownCompleteHintExample", ::markdownCompleteHintExample)

        // this is used by markdownActualizeExample
        add("markdown-actualized-example", ::actualizedFromMarkdownExample)

        // ----------------------------------------------------------------------------
        // Menu
        // ----------------------------------------------------------------------------

        add("menuBasic", ::menuBasic)
        add("menuPrimary", ::menuPrimary)
        add("menuContext", ::menuContext)
        add("menuItemActions", ::menuItemActions)

        this += WorkspaceRecipePaneFragmentFactory

        // ----------------------------------------------------------------------------
        // MPW
        // ----------------------------------------------------------------------------

        add("mpwAllPanesExample", ::mpwAllPanesExample)
        add("mpwToolPaneExample", ::mpwToolPaneExampleWrapper)
        add("mpwDoubleShiftExample", ::mpwDoubleShiftExample)

        // ----------------------------------------------------------------------------
        // Paragraph
        // ----------------------------------------------------------------------------

        add("paragraphBasicExample", ::paragraphBasicExample)
        add("paragraphLongExample", ::paragraphLongExample)
        add("paragraphNavigatorExample", ::paragraphNavigatorExample)

        // ----------------------------------------------------------------------------
        // Popup modal
        // ----------------------------------------------------------------------------

        add("modalPopupStandard", ::modalPopupStandard)
        add("modalPopupCustom", ::modalPopupCustom)
        add("modalPopupMultiPage", ::modalPopupMultiPage)

        // ----------------------------------------------------------------------------
        // Popup
        // ----------------------------------------------------------------------------

        add("popupPlayground", ::popupPlayground)

        // ----------------------------------------------------------------------------
        // Producer
        // ----------------------------------------------------------------------------

        add("mediaMetricsBasicExample", ::mediaMetricsBasicExample)
        add("hoverBasicExample", ::hoverBasicExample)
        add("focusBasicExample", ::focusBasicExample)

        // ----------------------------------------------------------------------------
        // Select
        // ----------------------------------------------------------------------------

        add("selectInputDropdownExample", ::selectInputDropdownExample)
        add("selectInputTextExample", ::selectInputTextExample)
        add("selectInputIconAndTextExample", ::selectInputIconAndTextExample)
        add("selectInputCheckboxExample", ::selectInputCheckboxExample)
        add("selectInputCustomExample", ::selectInputCustomExample)

        add("selectEditorMappingExample", ::selectEditorMappingExample)
        add("selectEditorRefNameExample", ::selectEditorRefNameExample)
        add("selectEditorRefMultiExample", ::selectEditorRefMultiExample)
        add("selectEditorRefPathExample", ::selectEditorRefPathExample)
        add("selectEditorRefManualExample", ::selectEditorRefManualExample)

        add("example/input/select/playground", ::selectInputPlayground)

        // ----------------------------------------------------------------------------
        // Status
        // ----------------------------------------------------------------------------

        add("statusInputSingleExample", ::statusInputSingleExample)
        add("statusEditorSingleExample", ::statusEditorSingleExample)

        // ----------------------------------------------------------------------------
        // Text Input
        // ----------------------------------------------------------------------------

        add("textInputSimpleExample", ::textInputSimpleExample)
        add("textInputAreaExample", ::textInputAreaExample)

        add("example/input/text/playground", ::textInputPlayground)

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

        add("example/tree/playground", ::treePlayground)

        // ----------------------------------------------------------------------------
        // Value
        // ----------------------------------------------------------------------------

        add("valueViewBackendTreeExample", ::valueViewBackendTreeExample)

        // ----------------------------------------------------------------------------
        // Xlsx
        // ----------------------------------------------------------------------------

        add("xlsxDownloadExample", ::xlsxDownloadExample)

    }
}