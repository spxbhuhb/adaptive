package `fun`.adaptive.sandbox.recipe.ui.input.select

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.document.ui.direct.markdownHint
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.sandbox.support.configureForm
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.editor.intEditor
import `fun`.adaptive.ui.editor.selectEditor
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.AdatFormViewBackend
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.input.select.item.selectInputItemCheckbox
import `fun`.adaptive.ui.input.select.item.selectInputItemIconAndText
import `fun`.adaptive.ui.input.select.item.selectInputItemText
import `fun`.adaptive.ui.input.select.selectInput
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun selectInputPlayground(): AdaptiveFragment {

    val form = adatFormBackend(SelectRecipeConfig())
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
class SelectRecipeConfig(
    val dataset: String = datasets.first(),
    val itemRenderer: String = itemRenderers.first(),
    val optionCount: Int = 30,
    val label: String? = null,
    val isInConstraintError: Boolean = false,
    val isDisabled: Boolean = false,
    val isMultiSelect: Boolean = false,
    val withSurfaceContainer: Boolean = true
)

@Adaptive
fun selectInputPlaygroundForm(
    form: AdatFormViewBackend<SelectRecipeConfig>
) {

    val template = SelectRecipeConfig()

    configureForm(form) {
        width { 288.dp }

        column {
            gap { 16.dp } .. padding { 16.dp }

            row {
                gap { 16.dp }

                selectEditor(datasets, { selectInputItemCheckbox(it) }) { template.dataset } .. width { 120.dp }

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
                }

                selectEditor(itemRenderers, { selectInputItemCheckbox(it) }) { template.itemRenderer } .. width { 256.dp }
            }
        }
    }

}

@Adaptive
fun selectInputPlaygroundResult(config: SelectRecipeConfig) {

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

    column {
        width { 240.dp } .. height { 240.dp }
        selectInput(backend) {
            when (config.itemRenderer) {
                "Text only" -> selectInputItemText(it)
                "Icon and text" -> selectInputItemIconAndText(it)
                "Checkbox" -> selectInputItemCheckbox(it)
            }
        } .. maxSize
    }
}