package `fun`.adaptive.doc.example.textInput

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.doc.support.configureForm
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.AdatFormViewBackend
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun textInputPlayground(): AdaptiveFragment {

    val form = observe { adatFormBackend(_root_ide_package_.`fun`.adaptive.doc.example.textInput.TextPlaygroundConfig()) }

    flowBox {
        gap { 16.dp }
        _root_ide_package_.`fun`.adaptive.doc.example.textInput.textInputPlaygroundForm(form)
        _root_ide_package_.`fun`.adaptive.doc.example.textInput.textInputPlaygroundResult(form.inputValue)
    }

    return fragment()
}


@Adat
class TextPlaygroundConfig(
    val label: String? = null,
    val isInConstraintError: Boolean = false,
    val isDisabled: Boolean = false,
    val multiline : Boolean = false,
)

@Adaptive
fun textInputPlaygroundForm(
    form: AdatFormViewBackend<`fun`.adaptive.doc.example.textInput.TextPlaygroundConfig>
) {

    val template = _root_ide_package_.`fun`.adaptive.doc.example.textInput.TextPlaygroundConfig()

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
                    booleanEditor { template.multiline }
                }
            }
        }
    }

}

@Adaptive
fun textInputPlaygroundResult(config: `fun`.adaptive.doc.example.textInput.TextPlaygroundConfig) {

    val backend = textInputBackend {
        disabled = config.isDisabled
        label = config.label
        validateFun = { ! config.isInConstraintError }
        multiline = config.multiline
    }.also {
        if (config.isInConstraintError) {
            it.isInConstraintError = true
            it.isTouched = true
        }
    }

    column {
        width { 240.dp } .. height { 240.dp }
        textInput(backend) .. maxSize
    }
}