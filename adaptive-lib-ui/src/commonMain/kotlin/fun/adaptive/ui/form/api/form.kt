package `fun`.adaptive.ui.form.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.api.isValid
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.repeat
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.wireformat.signature.KotlinSignatures
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.form.api.field.boolean
import `fun`.adaptive.ui.form.api.field.textual
import `fun`.adaptive.ui.editor.theme.editorTheme
import `fun`.adaptive.wireformat.signature.DatetimeSignatures

@Adaptive
fun form(data: AdatClass, vararg instructions : AdaptiveInstruction) : AdaptiveFragment {
    require(data.getMetadata().isMutableClass || data.adatContext?.store != null) {
        "${data.adatCompanion.wireFormatName} is immutable without a store"
    }

    val propertyCount = data.getMetadata().properties.size

    grid(*instructions) {
        width { 600.dp } .. height { (propertyCount * 44 + (propertyCount - 1) * 24).dp }
        colTemplate(200.dp, 400.dp) .. rowTemplate(44.dp repeat propertyCount) .. gap { 24.dp }

        for (property in data.getMetadata().properties) {
            row {
                maxHeight .. alignItems.startCenter
                text(property.name.replaceFirstChar { it.uppercaseChar() })
            }
            row {
                gap { 16.dp } .. maxHeight .. alignItems.startCenter

                when (property.signature) {
                    KotlinSignatures.BYTE,
                    KotlinSignatures.SHORT,
                    KotlinSignatures.INT,
                    KotlinSignatures.LONG,
                    KotlinSignatures.FLOAT,
                    KotlinSignatures.DOUBLE,
                    KotlinSignatures.CHAR,
                    KotlinSignatures.STRING
                        -> textual(data, property) .. editorTheme.enabled

                    KotlinSignatures.BOOLEAN
                        -> boolean(data, property)

                    DatetimeSignatures.INSTANT,
                    KotlinSignatures.UUID
                        -> text(data.getValue(property.index).toString()) .. editorTheme.disabled .. width { 350.dp }

                    DatetimeSignatures.LOCAL_DATE_TIME
                        -> textual(data, property) .. editorTheme.enabled

                    else -> {
                        if (property.isAdatClass) {
                            if (property.isImmutableProperty) {
                                text(data.getValue(property.index).toString())
                            } else {
                                subForm(data, property)
                            }
                        }
                    }
                }

                if (! data.isValid(property.name)) {
                    text("invalid value") .. textColor(0xFF0000u) .. zIndex(200)
                }
            }
        }
    }

    return fragment()
}