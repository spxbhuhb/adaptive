package `fun`.adaptive.ui.fragment.structural

interface PopupSourceViewBackend {

    var isPopupOpen: Boolean

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