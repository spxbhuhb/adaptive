package `fun`.adaptive.ui.menu

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.ui.fragment.structural.PopupSourceViewBackend
import kotlin.properties.Delegates.observable

class MenuViewBackend<T>(
    items: List<MenuItemBase<T>>,
    theme: MenuTheme,
    selectedFun: MenuEventHandler<T>,
) : SelfObservable<MenuViewBackend<T>>(), PopupSourceViewBackend {

    var items by observable(items, ::notify)
    var theme by observable(theme, ::notify)
    var selectedFun by observable(selectedFun, ::notify)

    /**
     * Close the menu automatically after [selectedFun] is called.
     */
    var autoClose by observable(true, ::notify)

    override var isPopupOpen: Boolean = false

    override var hidePopup: (() -> Unit)? = null

}
