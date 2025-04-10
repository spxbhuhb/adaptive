package `fun`.adaptive.ui.platform.input

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.Name
import `fun`.adaptive.foundation.query.first
import `fun`.adaptive.ui.AbstractAuiFragment
import org.w3c.dom.HTMLInputElement

actual fun findActualInputValue(fragment: AdaptiveFragment, name: String): String {
    val editor = fragment.first { it.instructions.any { it is Name && it.name == name } }
    val input = editor.first { it is AbstractAuiFragment<*> && it.receiver is HTMLInputElement }
    val receiver = (input as AbstractAuiFragment<*>).receiver as HTMLInputElement
    return receiver.value
}