package `fun`.adaptive.ui.render

import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.RawSurrounding
import `fun`.adaptive.ui.instruction.event.OnClick
import `fun`.adaptive.ui.instruction.event.OnMove
import `fun`.adaptive.ui.instruction.event.OnPrimaryDown
import `fun`.adaptive.ui.instruction.event.OnPrimaryUp
import `fun`.adaptive.ui.instruction.event.UIEvent
import `fun`.adaptive.ui.instruction.event.UIEventHandler
import `fun`.adaptive.ui.platform.BrowserEventListener
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent

object BrowserInputApplier : InputRenderApplier<HTMLElement>() {

    override fun applyTabIndex(receiver: HTMLElement, tabIndex: Int?) {
        receiver.tabIndex = tabIndex ?: -1
    }

}