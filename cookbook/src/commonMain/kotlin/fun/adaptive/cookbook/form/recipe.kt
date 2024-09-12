package `fun`.adaptive.cookbook.form

import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.form.api.form
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowText
import `fun`.adaptive.ui.api.gap
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