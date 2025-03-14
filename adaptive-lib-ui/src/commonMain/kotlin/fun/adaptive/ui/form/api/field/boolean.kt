package `fun`.adaptive.ui.form.api.field

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.check
import `fun`.adaptive.ui.checkbox.api.CheckboxTheme
import `fun`.adaptive.ui.instruction.dp

/**
 * Editor for a boolean value, shows a checkbox.
 */
@Adaptive
fun boolean(data: AdatClass, property: AdatPropertyMetadata, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    row(instructions()) {
        noSelect .. maxHeight .. alignItems.startCenter
        onClick { data.setValue(property.index, ! (data.getValue(property.index) as Boolean)) }

        if (data.getValue(property.index) as Boolean) {
            box(CheckboxTheme.DEFAULT.active) {
                svg(Graphics.check) .. noSelect .. frame(1.dp, 1.dp, 18.dp, 18.dp)
            }
        } else {
            box(CheckboxTheme.DEFAULT.inactive) {

            }
        }
    }

    return fragment()
}