package `fun`.adaptive.sandbox.recipe.ui.form

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.adat.encodeToPrettyJson
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.sandbox.support.E
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.codefence.codeFence
import `fun`.adaptive.ui.editor.*
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.input.select.item.selectInputOptionCheckbox
import `fun`.adaptive.ui.input.select.item.selectInputOptionText
import `fun`.adaptive.ui.input.select.item.selectInputValueText
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.snackbar.infoNotification
import `fun`.adaptive.ui.snackbar.warningNotification
import `fun`.adaptive.utility.localDate
import `fun`.adaptive.utility.localDateTime
import `fun`.adaptive.utility.localTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

/**
 * # Basic form
 *
 * This example demonstrates a basic form with some built-in editors.
 *
 * You see the `inputValue` of the form on the right, it updates
 * as you edit the fields.
 *
 * The Adat class used for this from specifies some validation rules:
 *
 * - `String` must match the pattern `[a-z]+`
 * - `Int` must be greater than 10 and less than 100
 */
@Adaptive
fun formBasicExample(): AdaptiveFragment {
    val template = FormData()
    val form = observe { adatFormBackend(template) }

    localContext(form) {
        flowBox {
            maxWidth .. gap { 20.dp }

            column {
                gap { 16.dp } .. width { 250.dp }

                textEditor { template.string }

                booleanEditor { template.boolean }

                intEditor { template.int }
                longEditor { template.long }
                doubleEditor { template.double }

                dateEditor { template.date }
                timeEditor { template.time }
                dateTimeEditor { template.dateTime }

                selectEditorDropdown(
                    E.Companion.entries.toList(),
                    { selectInputOptionText(it) },
                    { selectInputValueText(it) }
                ) { template.enum }

                selectEditorList(
                    E.Companion.entries.toList(),
                    { selectInputOptionCheckbox(it) }
                ) { template.enumOrNull }


                badgeEditor { template.badges }

                button("Save") .. onClick {
                    if (form.isInvalid()) {
                        warningNotification("Form is not valid!")
                        return@onClick
                    }
                    infoNotification("Saved!")
                }
            }
            column {
                width { 250.dp } .. gap { 16.dp }

                text("Valid: ${form.isValid()}")
                codeFence(form.inputValue.encodeToPrettyJson()) .. maxWidth
            }
        }
    }

    return fragment()
}

@Adat
class FormData(
    val boolean: Boolean = true,
    val int: Int = 34,
    val long: Long = 45,
    val double: Double = 89.10,
    val string: String = "ab",
    val date : LocalDate = localDate(),
    val time : LocalTime = localTime(),
    val dateTime : LocalDateTime = localDateTime(),
    val enum: E = E.V1,
    val enumOrNull: E? = null,
    val badges : Set<String> = setOf("badge1", "badge2")
) {
    override fun descriptor() {
        properties {
            int maximum 100 minimum 10
            string pattern "[a-z]+"
        }
    }
}