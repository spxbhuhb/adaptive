package `fun`.adaptive.sandbox.recipe.ui.form

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.sandbox.support.E
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.editor.doubleEditor
import `fun`.adaptive.ui.editor.intEditor
import `fun`.adaptive.ui.editor.selectEditorDropdown
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.input.select.item.selectInputOptionText
import `fun`.adaptive.ui.input.select.item.selectInputValueText
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.snackbar.infoNotification
import `fun`.adaptive.ui.snackbar.warningNotification

@Adaptive
fun formBasicExample() : AdaptiveFragment {
    val template = FormData()
    val form = valueFrom { adatFormBackend(template) }

    localContext(form) {
        column {
            gap { 16.dp } .. width { 256.dp }

            booleanEditor { template.boolean }
            intEditor { template.int }
            doubleEditor { template.double }
            textEditor { template.string }

            selectEditorDropdown(
                E.entries.toList(),
                { selectInputOptionText(it) },
                { selectInputValueText(it) }
            ) { template.enum }

            selectEditorDropdown(
                E.entries.toList(),
                { selectInputOptionText(it) },
                { selectInputValueText(it) }
            ) { template.enumOrNull }

            column {
                text("Valid: ${form.isValid()}")
            }

            button("Save") .. onClick {
                if (form.isInvalid()) {
                    warningNotification("Form is not valid!")
                    return@onClick
                }
                infoNotification("Saved!")
            }
        }
    }

    return fragment()
}