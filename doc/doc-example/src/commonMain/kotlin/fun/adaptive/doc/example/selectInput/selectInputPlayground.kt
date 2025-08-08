package `fun`.adaptive.doc.example.selectInput

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.doc.example.generated.resources.eco
import `fun`.adaptive.doc.example.generated.resources.zigbee
import `fun`.adaptive.doc.support.configureForm
import `fun`.adaptive.document.ui.direct.markdownHint
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.editor.intEditor
import `fun`.adaptive.ui.editor.selectEditorList
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.AdatFormViewBackend
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.generated.resources.account_box
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.input.select.SingleSelectInputViewBackend
import `fun`.adaptive.ui.input.select.item.*
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.input.select.selectInputDropdown
import `fun`.adaptive.ui.input.select.selectInputList
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.utility.UUID.Companion.uuid7
import kotlin.math.min

@Adaptive
fun selectInputPlayground(
    @Suppress("unused")
    args : Map<String, String>
): AdaptiveFragment {

    val form = observe { adatFormBackend(_root_ide_package_.`fun`.adaptive.doc.example.selectInput.SelectPlaygroundConfig()) }

    flowBox {
        gap { 16.dp }
        _root_ide_package_.`fun`.adaptive.doc.example.selectInput.selectInputPlaygroundForm(form)
        _root_ide_package_.`fun`.adaptive.doc.example.selectInput.selectInputPlaygroundResult(form.inputValue)
    }

    return fragment()
}

class Option(
    val text: String,
    val icon: GraphicsResourceSet
)

val datasets = listOf("Options", "Networks", "Roles")
val itemRenderers = listOf("Text only", "Icon and text", "Checkbox")


@Adat
class SelectPlaygroundConfig(
    val dataset: String = _root_ide_package_.`fun`.adaptive.doc.example.selectInput.datasets.first(),
    val itemRenderer: String = _root_ide_package_.`fun`.adaptive.doc.example.selectInput.itemRenderers.first(),
    val optionCount: Int = 30,
    val selectItem: Int? = null,
    val label: String? = null,
    val isInConstraintError: Boolean = false,
    val isDisabled: Boolean = false,
    val isMultiSelect: Boolean = false,
    val withSurfaceContainer: Boolean = false,
    val withDropdown: Boolean = true
)

@Adaptive
fun selectInputPlaygroundForm(
    form: AdatFormViewBackend<`fun`.adaptive.doc.example.selectInput.SelectPlaygroundConfig>
) {

    val template = _root_ide_package_.`fun`.adaptive.doc.example.selectInput.SelectPlaygroundConfig()

    configureForm(form) {
        width { 288.dp }

        column {
            gap { 16.dp } .. padding { 16.dp }

            row {
                gap { 16.dp }

                selectEditorList(_root_ide_package_.`fun`.adaptive.doc.example.selectInput.datasets, { selectInputOptionCheckbox(it) }) { template.dataset } .. width { 120.dp }

                column {
                    gap { 8.dp } .. width { 120.dp }
                    intEditor { template.optionCount }
                    markdownHint("300ish is fine, at 1000 it starts to be slow in Safari")
                }
            }

            column {
                gap { 8.dp } .. width { 256.dp }
                textEditor { template.label }

                column { // this column does not use gap, so boolean options look nice
                    maxWidth
                    booleanEditor { template.isInConstraintError }
                    booleanEditor { template.isDisabled }
                    booleanEditor { template.isMultiSelect }
                    booleanEditor { template.withSurfaceContainer }
                    booleanEditor { template.withDropdown }
                }

                selectEditorList(_root_ide_package_.`fun`.adaptive.doc.example.selectInput.itemRenderers, { selectInputOptionCheckbox(it) }) { template.itemRenderer } .. width { 256.dp }
            }

            column {
                gap { 8.dp } .. width { 120.dp }
                intEditor { template.selectItem }
                markdownHint("pre-select the item with this index")
            }
        }
    }

}

@Adaptive
fun selectInputPlaygroundResult(config: `fun`.adaptive.doc.example.selectInput.SelectPlaygroundConfig) {

    val effectiveOptions = when (config.dataset) {
        "Options" -> (1 .. config.optionCount).map { _root_ide_package_.`fun`.adaptive.doc.example.selectInput.Option("Option $it", Graphics.menu_book) }
        "Networks" -> _root_ide_package_.`fun`.adaptive.doc.example.selectInput.networkOptions.map { _root_ide_package_.`fun`.adaptive.doc.example.selectInput.Option(it.first, it.second) }
        "Roles" -> _root_ide_package_.`fun`.adaptive.doc.example.selectInput.roleOptions.map { _root_ide_package_.`fun`.adaptive.doc.example.selectInput.Option(it.second, Graphics.empty) }
        else -> emptyList()
    }

    val selectedItem = config.selectItem?.let { effectiveOptions[min(it, effectiveOptions.size - 1)] }

    val backend = selectInputBackend(selectedItem) {
        options = effectiveOptions
        disabled = config.isDisabled
        label = config.label
        withSurfaceContainer = config.withSurfaceContainer
        multiSelect = config.isMultiSelect
        toText = { it.text }
        toIcon = { it.icon }
        validateFun = { ! config.isInConstraintError }
    }.also {
        if (config.isInConstraintError) {
            it.isInConstraintError = true
            it.isTouched = true
        }
    }

    if (config.withDropdown) {
        column {
            width { 240.dp }
            _root_ide_package_.`fun`.adaptive.doc.example.selectInput.actualInputDropdown(backend, config) .. maxWidth
        }
    } else {
        column {
            width { 240.dp } .. height { 240.dp }
            _root_ide_package_.`fun`.adaptive.doc.example.selectInput.actualInputList(backend, config) .. maxSize
        }
    }
}

@Adaptive
fun actualInputList(
    backend: SingleSelectInputViewBackend<`fun`.adaptive.doc.example.selectInput.Option, `fun`.adaptive.doc.example.selectInput.Option, `fun`.adaptive.doc.example.selectInput.Option>, // FIXME the first option here is a lie actually, considering multi-select
    config: `fun`.adaptive.doc.example.selectInput.SelectPlaygroundConfig
): AdaptiveFragment {
    selectInputList(
        backend
    ) {
        when (config.itemRenderer) {
            "Text only" -> selectInputOptionText(it)
            "Icon and text" -> selectInputOptionIconAndText(it)
            "Checkbox" -> selectInputOptionCheckbox(it)
        }
    } .. instructions()
    return fragment()
}

@Adaptive
fun actualInputDropdown(
    backend: SingleSelectInputViewBackend<*, `fun`.adaptive.doc.example.selectInput.Option, `fun`.adaptive.doc.example.selectInput.Option>,
    config: `fun`.adaptive.doc.example.selectInput.SelectPlaygroundConfig
): AdaptiveFragment {
    selectInputDropdown(
        backend,
        {
            when (config.itemRenderer) {
                "Text only" -> selectInputOptionText(it)
                "Icon and text" -> selectInputOptionIconAndText(it)
                "Checkbox" -> selectInputOptionCheckbox(it)
            }
        },
        {
            when (config.itemRenderer) {
                "Text only" -> selectInputValueText(it)
                "Icon and text" -> selectInputValueIconAndText(it)
                "Checkbox" -> selectInputValueText(it)
            }
        }
    ) .. instructions()
    return fragment()
}

private val networkOptions = listOf(
    "Zigbee" to Graphics.zigbee,
    "Modbus" to Graphics.account_box,
    "SPXB" to Graphics.eco
)

private val roleOptions = listOf(
    uuid7<Any>() to "Content Manager",
    uuid7<Any>() to "Editor",
    uuid7<Any>() to "Viewer"
)