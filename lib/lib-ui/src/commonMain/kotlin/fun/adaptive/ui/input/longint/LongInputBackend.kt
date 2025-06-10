package `fun`.adaptive.ui.input.longint

fun longInputBackend(inputValue : Long? = null, builder: LongInputViewBackendBuilder.() -> Unit = {  }) =
    LongInputViewBackendBuilder(inputValue).apply(builder).toBackend()
