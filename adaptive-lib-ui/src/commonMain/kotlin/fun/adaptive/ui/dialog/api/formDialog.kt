package `fun`.adaptive.ui.dialog.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingBottom
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.width

import `fun`.adaptive.ui.builtin.check
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.form.api.form
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.OnClose
import `fun`.adaptive.utility.firstOrNullIfInstance

@Adaptive
fun formDialog(title : String, data : AdatClass, vararg instructions: AdaptiveInstruction): AdaptiveFragment {

    val onClose = fragment().instructions.firstOrNullIfInstance<OnClose>()

    dialog(title, instructions()) {

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