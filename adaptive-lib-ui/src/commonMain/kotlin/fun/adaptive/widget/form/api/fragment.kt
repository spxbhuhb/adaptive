package `fun`.adaptive.widget.form.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.api.isValid
import `fun`.adaptive.adat.api.validate
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.repeat
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.widget.form.form
import `fun`.adaptive.wireformat.signature.KotlinSignatures
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.frame
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.image
import `fun`.adaptive.ui.api.lightFont
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.noBorder
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.instruction.sp
import widget.lib.Res
import widget.lib.check

@Adaptive
fun form(data: AdatClass) {
    require(data.getMetadata().isMutableClass || data.adatContext?.store != null) {
        "${data.adatCompanion.wireFormatName} is immutable without a store"
    }

    val propertyCount = data.getMetadata().properties.size

    println(data.isValid())

    grid {
        width { 600.dp } .. height { (propertyCount * 44 + (propertyCount - 1) * 8).dp }
        colTemplate(200.dp, 400.dp) .. rowTemplate(44.dp repeat propertyCount) .. gap { 8.dp }

        for (property in data.getMetadata().properties) {
            row {
                maxHeight .. alignItems.startCenter
                text(property.name.replaceFirstChar { it.uppercaseChar() })
            }
            row {
                gap { 16.dp }

                when (property.signature) {
                    KotlinSignatures.BYTE,
                    KotlinSignatures.SHORT,
                    KotlinSignatures.INT,
                    KotlinSignatures.LONG,
                    KotlinSignatures.FLOAT,
                    KotlinSignatures.DOUBLE,
                    KotlinSignatures.CHAR,
                    KotlinSignatures.STRING
                        -> inputField(data, property) .. fieldStyle

                    KotlinSignatures.BOOLEAN
                        -> checkbox(data, property)
                }

                if (!data.isValid(property.name)) {
                    text("invalid value") .. textColor(0xFF0000u) .. zIndex(200) .. alignSelf.startCenter
                }
            }
        }
    }
}

/**
 * Editor for a boolean.
 */
@Adaptive
fun checkbox(data: AdatClass, property: AdatPropertyMetadata, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    row(*instructions) {
        noSelect .. maxHeight .. alignItems.startCenter
        onClick { data.setValue(property.index, ! (data.getValue(property.index) as Boolean)) }

        if (data.getValue(property.index) as Boolean) {
            box(*activeCheckBox) {
                image(Res.drawable.check) .. noSelect .. frame(1.dp, 1.dp, 18.dp, 18.dp)
            }
        } else {
            box(*inactiveCheckBox) {

            }
        }
    }

    return fragment()
}

@AdaptiveExpect(form)
fun inputField(data: AdatClass, property: AdatPropertyMetadata, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    manualImplementation(data, property, instructions)
}

internal val white = color(0xffffffu)
internal val purple = color(0xA644FFu)

internal val fieldStyle = instructionsOf(
    // PlaceholderColor(0x8A8A8F),
    textColor(0x000000u),
    backgroundColor(0xEFEFF4u),
    cornerRadius(8.dp),
    noBorder,
    height { 44.dp },
    fontSize { 17.sp },
    lightFont,
    padding(left = 16.dp, right = 16.dp)
)


internal var activeCheckBox = instructionsOf(
    size(20.dp, 20.dp),
    cornerRadius(10.dp),
    backgroundColor(purple),
    textColor(white)
)

internal var inactiveCheckBox = instructionsOf(
    size(20.dp, 20.dp),
    cornerRadius(10.dp),
    border(purple, 1.dp)
)