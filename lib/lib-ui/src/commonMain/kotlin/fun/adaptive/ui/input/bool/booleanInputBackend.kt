package `fun`.adaptive.ui.input.bool

fun booleanInputBackend(inputValue : Boolean? = null, builder: BooleanInputViewBackendBuilder.() -> Unit = {  }) =
    BooleanInputViewBackendBuilder(inputValue).apply(builder).toBackend()
