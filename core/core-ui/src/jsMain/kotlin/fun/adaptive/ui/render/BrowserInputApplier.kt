package `fun`.adaptive.ui.render

import org.w3c.dom.HTMLElement

object BrowserInputApplier : InputRenderApplier<HTMLElement>() {

    override fun applyTabIndex(receiver: HTMLElement, tabIndex: Int?) {
        receiver.tabIndex = tabIndex ?: -1
    }

}