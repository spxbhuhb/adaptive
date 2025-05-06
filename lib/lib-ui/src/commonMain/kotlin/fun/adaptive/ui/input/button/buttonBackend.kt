package `fun`.adaptive.ui.input.button

fun buttonBackend(builder: ButtonViewBackendBuilder.() -> Unit = { }) =
    ButtonViewBackendBuilder().apply(builder).toBackend()
