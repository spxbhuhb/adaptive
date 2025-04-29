package `fun`.adaptive.sandbox.recipe.ui.input.select

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.document.ui.direct.markdownHint
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.sandbox.support.configureForm
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.editor.intEditor
import `fun`.adaptive.ui.editor.selectEditorList
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.AdatFormViewBackend
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.input.select.SingleSelectInputViewBackend
import `fun`.adaptive.ui.input.select.item.selectInputOptionCheckbox
import `fun`.adaptive.ui.input.select.item.selectInputOptionIconAndText
import `fun`.adaptive.ui.input.select.item.selectInputOptionText
import `fun`.adaptive.ui.input.select.item.selectInputValueIconAndText
import `fun`.adaptive.ui.input.select.item.selectInputValueText
import `fun`.adaptive.ui.input.select.selectInputList
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.input.select.selectInputDropdown
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun selectInputPlayground(): AdaptiveFragment {

    val form = adatFormBackend(SelectPlaygroundConfig())
    val config = valueFrom { form }

    flowBox {
        gap { 16.dp }
        selectInputPlaygroundForm(form)
        selectInputPlaygroundResult(config)
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
    val dataset: String = datasets.first(),
    val itemRenderer: String = itemRenderers.first(),
    val optionCount: Int = 30,
    val label: String? = null,
    val isInConstraintError: Boolean = false,
    val isDisabled: Boolean = false,
    val isMultiSelect: Boolean = false,
    val withSurfaceContainer: Boolean = false,
    val withDropdown: Boolean = true
)

@Adaptive
fun selectInputPlaygroundForm(
    form: AdatFormViewBackend<SelectPlaygroundConfig>
) {

    val template = SelectPlaygroundConfig()

    configureForm(form) {
        width { 288.dp }

        column {
            gap { 16.dp } .. padding { 16.dp }

            row {
                gap { 16.dp }

                selectEditorList(datasets, { selectInputOptionCheckbox(it) }) { template.dataset } .. width { 120.dp }

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

                selectEditorList(itemRenderers, { selectInputOptionCheckbox(it) }) { template.itemRenderer } .. width { 256.dp }
            }
        }
    }

}

@Adaptive
fun selectInputPlaygroundResult(config: SelectPlaygroundConfig) {

    val effectiveOptions = when (config.dataset) {
        "Options" -> (1 .. config.optionCount).map { Option("Option $it", Graphics.menu_book) }
        "Networks" -> networkOptions.map { Option(it.first, it.second) }
        "Roles" -> roleOptions.map { Option(it.second, Graphics.empty) }
        else -> emptyList()
    }

    val backend = selectInputBackend<Option> {
        options = effectiveOptions
        disabled = config.isDisabled
        label = config.label
        withSurfaceContainer = config.withSurfaceContainer
        withDropdown = config.withDropdown
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
            actualInputDropdown(backend, config) .. maxWidth
        }
    } else {
        column {
            width { 240.dp } .. height { 240.dp }
            actualInputList(backend, config) .. maxSize
        }
    }
}

@Adaptive
fun actualInputList(
    backend: SingleSelectInputViewBackend<Option, Option>,
    config: SelectPlaygroundConfig
): AdaptiveFragment {
    selectInputList(
        backend,
        {
            when (config.itemRenderer) {
                "Text only" -> selectInputOptionText(it)
                "Icon and text" -> selectInputOptionIconAndText(it)
                "Checkbox" -> selectInputOptionCheckbox(it)
            }
        }
    ) .. instructions()
    return fragment()
}

@Adaptive
fun actualInputDropdown(
    backend: SingleSelectInputViewBackend<Option, Option>,
    config: SelectPlaygroundConfig
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