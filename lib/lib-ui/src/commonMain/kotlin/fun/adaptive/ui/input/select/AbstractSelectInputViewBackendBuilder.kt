package `fun`.adaptive.ui.input.select

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.input.InputViewBackendBuilder

abstract class AbstractSelectInputViewBackendBuilder<SVT,IVT,OT>(
    inputValue: SVT?
) : InputViewBackendBuilder<SVT, AbstractSelectInputViewBackend<SVT,IVT,OT>>(inputValue) {

    var options : List<OT>? = null
    var multiSelect : Boolean? = null

    var toText: ((OT) -> String)? = null
    var toIcon: ((OT) -> GraphicsResourceSet)? = null

    var listInputTheme: SelectInputTheme? = null
    var withSurfaceContainer: Boolean? = null
    var withDropdown: Boolean? = null

    override fun setup(backend : AbstractSelectInputViewBackend<SVT,IVT,OT>) {
        super.setup(backend)
        options?.let { backend.options = it }
        multiSelect?.let { backend.isMultiSelect = it }
        toText?.let { backend.toText = it }
        toIcon?.let { backend.toIcon = it }
        listInputTheme?.let { backend.listInputTheme = it }
        withSurfaceContainer?.let { backend.withSurfaceContainer = it }
        withDropdown?.let { backend.withDropDown = it }
    }
}