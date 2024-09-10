package `fun`.adaptive.cookbook.form

import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.widget.form.api.form
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowText
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.instruction.dp


@Adaptive
fun formMain() {
    val data = copyStore { FormData() }
    column {
        gap(16.dp)
        form(data)
        flowText(data)
    }
}