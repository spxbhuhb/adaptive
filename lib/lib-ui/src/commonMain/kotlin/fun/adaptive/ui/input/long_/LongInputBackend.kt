package `fun`.adaptive.ui.input.long_

fun longInputBackend(inputValue : Long? = null, builder: LongInputViewBackendBuilder.() -> Unit = {  }) =
    LongInputViewBackendBuilder(inputValue).apply(builder).toBackend()
