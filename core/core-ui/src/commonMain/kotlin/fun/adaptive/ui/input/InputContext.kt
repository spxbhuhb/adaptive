package `fun`.adaptive.ui.input

import `fun`.adaptive.general.SelfObservable
import kotlin.properties.Delegates

class InputContext(
    invalid : Boolean = false,
    disabled: Boolean = false
) : SelfObservable<InputContext>() {

    var invalid by Delegates.observable(invalid, ::notify)
    var disabled by Delegates.observable(disabled, ::notify)
    var popupOpen by Delegates.observable(false, ::notify)

    /**
     * Focus the parent container when the popup loses focus.
     * This is in effect when the user click outside the popup.
     */
    val focusContainerOnPopupFocusOut : Boolean = false

    /**
     * Focus the parent container when the popup is programmatically
     * closed (by a button for example).
     */
    val focusContainerOnPopupClose: Boolean = true

    companion object {
        val DISABLED = InputContext(disabled = true)
    }

}