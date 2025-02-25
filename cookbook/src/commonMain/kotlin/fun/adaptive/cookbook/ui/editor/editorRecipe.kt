package `fun`.adaptive.cookbook.ui.editor

import `fun`.adaptive.cookbook.model.E
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.repeat
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.utility.localDate
import `fun`.adaptive.utility.localDateTime
import `fun`.adaptive.utility.localTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Adaptive
fun editorRecipe(): AdaptiveFragment {
    var hasInvalidInput = false
    var lastCheck = localDateTime()
    val fragment = fragment()

    column {
        row {
            gap(16.dp) .. alignItems.startCenter

            button("Check") .. onClick {
                hasInvalidInput = hasInvalidInput(fragment)
                lastCheck = localDateTime()
            }
            text(lastCheck)
            text(if (hasInvalidInput) "There is at least one invalid input." else "All inputs are valid.")
        }

        grid {
            colTemplate(100.dp, 400.dp, 1.fr) .. alignItems.startCenter .. gap { 16.dp }
            rowTemplate(44.dp repeat 5)

            textEditor()
            intEditor()
            timeEditor()
            dateEditor()
            enumEditor()
        }
    }

    return fragment()
}


@Adaptive
fun textEditor() {
    var value = "Hello World!"
    text("String")
    editor { value }
    text("Current value: $value")
}

@Adaptive
fun intEditor() {
    var value = 12
    text("Int")
    editor { value }
    text("Current value: $value")
}

@Adaptive
fun timeEditor() {
    var value = localTime()
    text("LocalTime")
    editor { value }
    text("Current value: $value")
}

@Adaptive
fun dateEditor() {
    var value = localDate()
    text("LocalDate")
    editor { value }
    text("Current value: $value")
}

@Adaptive
fun enumEditor() {
    var value = E.V1

    text("Enum")
    editor { value }
    text("Current value: $value")
}

fun hasInvalidInput(fragment: AdaptiveFragment): Boolean {
    if (fragment is AbstractAuiFragment<*> && fragment.invalidInput) return true

    for (child in fragment.children) {
        if (hasInvalidInput(child)) return true
    }

    return false
}