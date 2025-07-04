import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.date.dateInput
import `fun`.adaptive.ui.input.date.dateInputBackend
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.utility.localDate

@Adaptive
fun datePickerMain(): AdaptiveFragment {

    val vb1 = dateInputBackend(localDate())
    val vb2 = dateInputBackend(localDate())

    val text = textInputBackend("Hello World!") {}

    column {
        padding { 16.dp } .. maxHeight .. width { 400.dp } .. verticalScroll

        withLabel("Text") {
            textInput(text)
        }

        dateInput(vb1)
        dateInput(vb2)
    }

    return fragment()
}

