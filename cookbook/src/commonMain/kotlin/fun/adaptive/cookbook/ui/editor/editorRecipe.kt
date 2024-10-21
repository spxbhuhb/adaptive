package `fun`.adaptive.cookbook.ui.editor

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.rangeTo
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
import `fun`.adaptive.utility.localDateTime

@Adaptive
fun editorRecipe() {
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
            rowTemplate(44.dp repeat 2)

            textEditor()
            intEditor()
        }
    }
}


@Adaptive
fun textEditor() {
    var value: String = "Hello World!"
    text("String")
    editor { value }
    text("Current value: $value")
}

@Adaptive
fun intEditor() {
    var value: Int = 12
    text("Int")
    editor { value }
    text("Current value: $value")
}


fun hasInvalidInput(fragment : AdaptiveFragment): Boolean {
    if (fragment is AbstractAuiFragment<*> && fragment.invalidInput) return true

    for (child in fragment.children) {
        if (hasInvalidInput(child)) return true
    }

    return false
}