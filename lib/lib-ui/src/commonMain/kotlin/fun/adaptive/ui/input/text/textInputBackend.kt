package `fun`.adaptive.ui.input.text

fun textInputBackend(inputValue : String? = null, builder: TextInputViewBackendBuilder.() -> Unit = {  }) =
    TextInputViewBackendBuilder(inputValue).apply(builder).toBackend()
