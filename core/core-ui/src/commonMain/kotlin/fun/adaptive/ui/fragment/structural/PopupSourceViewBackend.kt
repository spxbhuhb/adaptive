package `fun`.adaptive.ui.fragment.structural

interface PopupSourceViewBackend {

    var isPopupOpen: Boolean

    /**
     * When shown, the popup will set the value of this property to a function
     * that hides the popup. This makes it very easy to close popups programmatically.
     *
     * When the popup is closed [hidePopup] is set to `null`.
     */
    var hidePopup : (() -> Unit)?

    /**
     * Focus the parent container when the popup loses focus.
     * This is in effect when the user clicks outside the popup.
     */
    val focusContainerOnPopupFocusOut: Boolean
        get() = false

    /**
     * Focus on the parent container when the popup is programmatically
     * closed (by a button, for example).
     */
    val focusContainerOnPopupClose: Boolean
        get() = true

}