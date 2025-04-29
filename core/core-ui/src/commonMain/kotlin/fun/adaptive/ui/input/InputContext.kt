package `fun`.adaptive.ui.input

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.ui.fragment.structural.PopupSourceViewBackend
import kotlin.properties.Delegates

class InputContext(
    invalid : Boolean = false,
    disabled: Boolean = false
) : SelfObservable<InputContext>(), PopupSourceViewBackend {

    var invalid by Delegates.observable(invalid, ::notify)
    var disabled by Delegates.observable(disabled, ::notify)

    override var isPopupOpen by Delegates.observable(false, ::notify)
    override var hidePopup: (() -> Unit)? = null

    companion object {
        val DISABLED = InputContext(disabled = true)
    }

}