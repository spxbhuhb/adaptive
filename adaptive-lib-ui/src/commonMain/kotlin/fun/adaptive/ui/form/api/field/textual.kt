package `fun`.adaptive.ui.form.api.field

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.ui.form.form

/**
 * Editor for a somewhat textual value, such as text and numbers.
 */
@AdaptiveExpect(form)
fun textual(data: AdatClass, property: AdatPropertyMetadata, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    manualImplementation(data, property, instructions)
}