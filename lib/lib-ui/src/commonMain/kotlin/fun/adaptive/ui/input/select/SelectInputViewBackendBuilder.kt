package `fun`.adaptive.ui.input.select

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.input.InputViewBackendBuilder

class SelectInputViewBackendBuilder<T>(
    inputValue: T?
) : InputViewBackendBuilder<T>(inputValue) {

    var options : List<T>? = null
    var multiSelect : Boolean? = null

    var toText: ((T) -> String)? = null
    var toIcon: ((T) -> GraphicsResourceSet)? = null

    var listInputTheme: SelectInputTheme? = null
    var withSurfaceContainer: Boolean? = null

    override fun toBackend() =
        SelectInputViewBackend(inputValue, label, secret).also { backend ->
            setup(backend)
            options?.let { backend.options = it }
            multiSelect?.let { backend.isMultiSelect = it }
            toText?.let { backend.toText = it }
            toIcon?.let { backend.toIcon = it }
            listInputTheme?.let { backend.listInputTheme = it }
            withSurfaceContainer?.let { backend.withSurfaceContainer = it }
        }

}