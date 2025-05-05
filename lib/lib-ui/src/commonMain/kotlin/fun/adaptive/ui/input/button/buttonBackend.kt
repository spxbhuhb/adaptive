package `fun`.adaptive.ui.input.button

fun buttonInputBackend(builder: ButtonViewBackendBuilder.() -> Unit = { }) =
    ButtonViewBackendBuilder().apply(builder).toBackend()
