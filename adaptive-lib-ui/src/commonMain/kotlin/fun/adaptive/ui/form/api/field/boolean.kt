package `fun`.adaptive.ui.form.api.field

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.frame
import `fun`.adaptive.ui.api.image
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.builtin.Res
import `fun`.adaptive.ui.builtin.check
import `fun`.adaptive.ui.checkbox.api.checkboxTheme
import `fun`.adaptive.ui.instruction.dp

/**
 * Editor for a boolean value, shows a checkbox.
 */
@Adaptive
fun boolean(data: AdatClass, property: AdatPropertyMetadata, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    row(*instructions) {
        noSelect .. maxHeight .. alignItems.startCenter
        onClick { data.setValue(property.index, ! (data.getValue(property.index) as Boolean)) }

        if (data.getValue(property.index) as Boolean) {
            box(*checkboxTheme.active) {
                image(Res.drawable.check) .. noSelect .. frame(1.dp, 1.dp, 18.dp, 18.dp)
            }
        } else {
            box(*checkboxTheme.inactive) {

            }
        }
    }

    return fragment()
}