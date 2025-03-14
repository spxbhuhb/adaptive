package `fun`.adaptive.ui.form

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.descriptor.general.Readonly
import `fun`.adaptive.adat.descriptor.general.UseToString
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.EditorTheme

@Adaptive
fun subForm(data: AdatClass, theme : EditorTheme, property: AdatPropertyMetadata): AdaptiveFragment {
    val value = data.getValue(property.index) as AdatClass

    if (data.hasDescriptor(property) { it is Readonly } && data.hasDescriptor(property) { it is UseToString }) {
        row {
            theme.enabled .. width { 400.dp } .. alignItems.startCenter
            text(value)
        }
    } else {
        form(value)
    }

    return fragment()
}

private fun AdatClass.hasDescriptor(property: AdatPropertyMetadata, condition: (it: AdatDescriptor) -> Boolean): Boolean {
    for (descriptor in adatCompanion.adatDescriptors.first { it.property.index == property.index }.descriptors) {
        if (condition(descriptor)) return true
    }
    return false
}

