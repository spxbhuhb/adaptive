package `fun`.adaptive.doc.example.button

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.doc.example.generated.resources.lock
import `fun`.adaptive.doc.support.configureForm
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.nop
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.log.devInfo
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.editor.doubleEditor
import `fun`.adaptive.ui.editor.selectEditorList
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.AdatFormViewBackend
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.generated.resources.check
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.input.button.buttonBackend
import `fun`.adaptive.ui.input.button.dangerButton
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.input.select.item.selectInputOptionCheckbox
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun buttonPlayground(): AdaptiveFragment {

    val form = observe { adatFormBackend(ButtonPlaygroundConfig()) }

    flowBox {
        gap { 16.dp }
        buttonPlaygroundForm(form)
        buttonPlaygroundResult(form.inputValue)
    }

    return fragment()
}

val variants = listOf("button", "submitButton", "dangerButton")
val icons = listOf("lock", "check", "account_circle")

@Adat
class ButtonPlaygroundConfig(
    val variant: String = variants.first(),
    val label: String? = "Hello",
    val icon: String? = icons.first(),
    val disabled: Boolean = false,
    val width: Double? = null
)

@Adaptive
fun buttonPlaygroundForm(
    form: AdatFormViewBackend<ButtonPlaygroundConfig>
) {

    val template = ButtonPlaygroundConfig()

    configureForm(form) {
        column {
            width { 288.dp } .. gap { 16.dp } .. padding { 16.dp }

            selectEditorList(variants, { selectInputOptionCheckbox(it) }) { template.variant }
            selectEditorList(icons, { selectInputOptionCheckbox(it) }) { template.icon }
            textEditor { template.label }
            doubleEditor { template.width }
            booleanEditor { template.disabled }
        }
    }
}

@Adaptive
fun buttonPlaygroundResult(config: ButtonPlaygroundConfig) {

    val icon = when (config.icon) {
        "lock" -> Graphics.lock
        "check" -> Graphics.check
        "account_circle" -> Graphics.account_circle
        else -> null
    }

    val width : AdaptiveInstruction = if (config.width != null) width { config.width.dp } else nop

    val backend = buttonBackend { disabled = config.disabled }

    when (config.variant) {
        "button" -> button(config.label, icon, backend) { devInfo("button clicked") } .. width
        "submitButton" -> submitButton(config.label, icon, backend) { devInfo("submitButton clicked") } .. width
        "dangerButton" -> dangerButton(config.label, icon, backend) { devInfo("dangerButton clicked") } .. width
    }
}