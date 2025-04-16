package `fun`.adaptive.ui.dialog

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.check
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.form.form
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.OnClose

@Adaptive
fun formDialog(title: String, data: AdatClass): AdaptiveFragment {

    val onClose = fragment().instructions.firstInstanceOfOrNull<OnClose>()

    deprecatedDialog(title) {
        instructions()

        column {
            padding { 32.dp }
            form(data)
        }

        column {
            width { 708.dp } .. alignItems.end .. paddingRight { 32.dp } .. paddingBottom { 32.dp }

            button("Save", Graphics.check) .. onClick {
                onClose?.handler?.invoke()
            }
        }

    }

    return fragment()
}