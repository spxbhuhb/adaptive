/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.form.fragment

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.get
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.instruction.input.InputPlaceholder
import `fun`.adaptive.utility.format
import `fun`.adaptive.ui.form.form
import `fun`.adaptive.wireformat.signature.DatetimeSignatures
import `fun`.adaptive.wireformat.signature.KotlinSignatures
import kotlinx.browser.document
import kotlinx.datetime.LocalDateTime
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

@AdaptiveActual(form)
open class FormTextual(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, 2, 3) {

    override val receiver: HTMLInputElement =
        document.createElement("input") as HTMLInputElement

    private val adatClass: AdatClass
        get() = state[0] as AdatClass

    private val property : AdatPropertyMetadata
        get() = state[1] as AdatPropertyMetadata

    // 2 -> instructions

    var propertyValue : String = ""

    lateinit var path : Array<String>

    override fun genPatchInternal(): Boolean {

        patchInstructions()

        if (haveToPatch(dirtyMask, 1)) {
            propertyValue = formatValue(adatClass.getValue(property.index))

            if (receiver.value != propertyValue) {
                receiver.value = propertyValue
            }
        }

        if (isInit) {
            path = arrayOf(property.name)

            setInputAttributes()

            receiver.addEventListener("input", {
                if (receiver.value != propertyValue) {
                    val parsedValue = parseValue(receiver.value)
                    adatClass.adatContext?.touch(path)
                    adatClass.setValue(path, parsedValue)
                }
            })
        }

        return false
    }

    fun formatValue(value: Any?) =
        when(property.signature) {
            KotlinSignatures.DOUBLE -> format(value as Double)
            KotlinSignatures.FLOAT -> format((value as Float).toDouble())
            else -> value.toString()
        }

    fun parseValue(value: String) =
        when(property.signature) {
            KotlinSignatures.BYTE -> value.toByteOrNull()
            KotlinSignatures.SHORT -> value.toShortOrNull()
            KotlinSignatures.INT -> value.toIntOrNull()
            KotlinSignatures.LONG -> value.toLongOrNull()
            KotlinSignatures.DOUBLE -> value.toDoubleOrNull()
            KotlinSignatures.FLOAT -> value.toFloatOrNull()
            KotlinSignatures.CHAR -> value.first()
            KotlinSignatures.STRING -> value
            DatetimeSignatures.LOCAL_DATE_TIME -> LocalDateTime.parse(value)
            else -> throw IllegalStateException("unsupported property type for $property")
        }

    fun setInputAttributes() {

        receiver.style.outline = "none"

        when(property.signature) {
            KotlinSignatures.BYTE -> {
                receiver.type = "number"
                receiver.min = Byte.MIN_VALUE.toString()
                receiver.max = Byte.MAX_VALUE.toString()
            }
            KotlinSignatures.SHORT -> {
                receiver.type = "number"
                receiver.min = Short.MIN_VALUE.toString()
                receiver.max = Short.MAX_VALUE.toString()
            }
            KotlinSignatures.INT -> {
                receiver.type = "number"
                receiver.min = Int.MIN_VALUE.toString()
                receiver.max = Int.MAX_VALUE.toString()
            }
            KotlinSignatures.LONG -> {
                receiver.type = "number"
                receiver.min = Long.MIN_VALUE.toString()
                receiver.max = Long.MAX_VALUE.toString()
            }
            KotlinSignatures.DOUBLE -> {
                receiver.type = "number"
                receiver.min = Double.MIN_VALUE.toString()
                receiver.max = Double.MAX_VALUE.toString()
            }
            KotlinSignatures.FLOAT -> {
                receiver.type = "number"
                receiver.min = Float.MIN_VALUE.toString()
                receiver.max = Float.MAX_VALUE.toString()
            }
            KotlinSignatures.CHAR -> {
                receiver.type = "text"
                receiver.maxLength = 1
            }
            KotlinSignatures.STRING -> {
                receiver.type = "text"
            }
            DatetimeSignatures.LOCAL_DATE_TIME -> {
                receiver.type = "datetime"
            }
            else -> throw IllegalStateException("unsupported property type for $property")
        }

        receiver.placeholder = get<InputPlaceholder>()?.value ?: property.name

        // TODO checking for the secret descriptor in input field is too expensive considering the number of uses in an application
        // however, if we will add more checks/customization properties it might be OK

        if (property.isSecret(adatClass.adatCompanion.adatDescriptors)) {
            receiver.type = "password"
        }
    }
}