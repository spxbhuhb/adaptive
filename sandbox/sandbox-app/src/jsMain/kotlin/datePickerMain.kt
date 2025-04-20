import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.datetime.dateInput
import `fun`.adaptive.ui.input.text.textInput2
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.utility.localDate

@Adaptive
fun datePickerMain(): AdaptiveFragment {

    var date = localDate()
    val text = textInputBackend("Hello World!") {}

    column {
        padding { 16.dp } .. maxHeight .. width { 400.dp } .. verticalScroll

        withLabel("Text") {
            textInput2(text)
        }

        withLabel("Date") {
            dateInput(date, it) { v -> date = v }
        }

        withLabel("Date", InputContext(invalid = true)) {
            dateInput(date, it) { v -> date = v }
        }
    }

    return fragment()
}

