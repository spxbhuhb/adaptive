package `fun`.adaptive.sandbox.recipe.ui.input.text

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.sandbox.support.configureForm
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.AdatFormViewBackend
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.input.text.textInput2
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun textInputPlayground(): AdaptiveFragment {

    val form = valueFrom { adatFormBackend(TextPlaygroundConfig()) }

    flowBox {
        gap { 16.dp }
        textInputPlaygroundForm(form)
        textInputPlaygroundResult(form.inputValue)
    }

    return fragment()
}


@Adat
class TextPlaygroundConfig(
    val label: String? = null,
    val isInConstraintError: Boolean = false,
    val isDisabled: Boolean = false
)

@Adaptive
fun textInputPlaygroundForm(
    form: AdatFormViewBackend<TextPlaygroundConfig>
) {

    val template = TextPlaygroundConfig()

    configureForm(form) {
        width { 288.dp }

        column {
            gap { 16.dp } .. padding { 16.dp }

            column {
                gap { 8.dp } .. width { 256.dp }
                textEditor { template.label }

                column { // this column does not use gap, so boolean options look nice
                    maxWidth
                    booleanEditor { template.isInConstraintError }
                    booleanEditor { template.isDisabled }
                }
            }
        }
    }

}

@Adaptive
fun textInputPlaygroundResult(config: TextPlaygroundConfig) {

    val backend = textInputBackend {
        disabled = config.isDisabled
        label = config.label
        validateFun = { ! config.isInConstraintError }
    }.also {
        if (config.isInConstraintError) {
            it.isInConstraintError = true
            it.isTouched = true
        }
    }

    column {
        width { 240.dp } .. height { 240.dp }
        textInput2(backend) .. maxSize
    }
}