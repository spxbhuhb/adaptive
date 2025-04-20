package `fun`.adaptive.sandbox.recipe.ui.input.select

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.cookbook.generated.resources.select_input
import `fun`.adaptive.cookbook.generated.resources.zigbee
import `fun`.adaptive.document.ui.basic.docDocument
import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.document.ui.direct.markdownHint
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.document.Documents
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
import `fun`.adaptive.ui.generated.resources.account_box
import `fun`.adaptive.ui.generated.resources.eco
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.input.select.*
import `fun`.adaptive.ui.input.select.item.selectInputItemCheckbox
import `fun`.adaptive.ui.input.select.item.selectInputItemIconAndText
import `fun`.adaptive.ui.input.select.item.selectInputItemText
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid7

@Adaptive
fun selectInputRecipe(): AdaptiveFragment {

    val form = adatFormBackend(SelectRecipeConfig())
    val config = valueFrom { form }

    column {
        gap { 16.dp } .. verticalScroll .. maxSize

        markdown(
            """
                # Select
                
                Select is a rather complex input with many configuration options and uses.
                
                ---
                
                ## Hard coded examples
            """.trimIndent()
        )

        selectInputTextExample()
        selectInputIconAndTextExample()
        selectInputCheckboxExample()

        markdown(
            """
            ---
            
            ## Playground
            
        """.trimIndent()
        )

        flowBox {
            gap { 16.dp }
            selectInputRecipeForm(form)
            selectInputConfigExample(config)
        }

        docDocument(Documents.select_input)

    }

    return fragment()
}

class Option(
    val text: String,
    val icon: GraphicsResourceSet
)

val optionsOptions = listOf(
    uuid7<Any>() to "Option 1",
    uuid7<Any>() to "Option 2",
    uuid7<Any>() to "Option 3"
)

val networkOptions = listOf(
    "Zigbee" to Graphics.zigbee,
    "Modbus" to Graphics.account_box,
    "SPXB" to Graphics.eco
)

val roleOptions = listOf(
    uuid7<Any>() to "Content Manager",
    uuid7<Any>() to "Editor",
    uuid7<Any>() to "Viewer"
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
fun selectInputRecipeForm(
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
fun selectInputConfigExample(config: SelectRecipeConfig) {

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


@Adaptive
fun selectInputTextExample() {

    val backend = selectInputBackend<Pair<UUID<Any>, String>> {
        options = optionsOptions
        withSurfaceContainer = true
        toText = { it.second }
    }

    row {
        fill.constrain .. gap { 16.dp } .. maxWidth

        column {
            width { 240.dp }
            selectInput(backend, { selectInputItemText(it) })
        }

        markdown(
            """
            * "Options" dataset    
            * text only item renderer
            * with surface container  
        """.trimIndent()
        )
    }

}

@Adaptive
fun selectInputIconAndTextExample() {

    val backend = selectInputBackend<Option> {
        this.options = networkOptions.map { Option(it.first, it.second) }
        toText = { it.text }
        toIcon = { it.icon }
    }

    row {
        fill.constrain .. gap { 16.dp } .. maxWidth

        column {
            width { 240.dp } .. verticalScroll .. backgroundColor(0xffff00, 0.3f) .. borders.outline .. padding { 8.dp }
            selectInput(backend, { selectInputItemIconAndText(it) })
        }

        markdown(
            """
            * "Networks" dataset
            * icon and text item renderer
            * manual styling, no focus, no feedback, etc.
        """.trimIndent()
        )
    }
}

@Adaptive
fun selectInputCheckboxExample() {

    val backend = selectInputBackend<Pair<UUID<Any>, String>> {
        this.options = roleOptions
        toText = { it.second }
    }

    row {
        fill.constrain .. gap { 16.dp } .. maxWidth

        column {
            width { 240.dp } .. verticalScroll .. padding { 8.dp }
            selectInput(backend, { selectInputItemCheckbox(it) })
        }

        markdown(
            """
            * "Roles" dataset
            * checkbox item renderer
            * manual styling
        """.trimIndent()
        )
    }

}