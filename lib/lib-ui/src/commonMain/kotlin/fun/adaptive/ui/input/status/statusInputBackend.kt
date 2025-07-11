package `fun`.adaptive.ui.input.status

import `fun`.adaptive.value.AvStatus

fun statusInputSingleBackend(inputValue: Set<AvStatus>? = null, status: AvStatus, builder: StatusInputSingleViewBackendBuilder.() -> Unit = { }) =
    StatusInputSingleViewBackendBuilder(status).also { it.inputValue = inputValue }.apply(builder).toBackend()
