package `fun`.adaptive.ui.api

import `fun`.adaptive.foundation.instruction.Name
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.AbstractContainer

fun AbstractAuiFragment<*>.dumpLayout(indent: String): String {
    return buildString {
        val name = (indent + (instructions.firstInstanceOfOrNull<Name>()?.name ?: this@dumpLayout::class.simpleName !!)).padEnd(40, ' ')
        val data = renderData

        appendLine("$name  top: ${data.finalTop.padded}    left: ${data.finalLeft.padded}    width: ${data.finalWidth.padded}    height: ${data.finalHeight.padded}")
        if (this@dumpLayout is AbstractContainer<*, *>) {
            layoutItems.forEach {
                append(it.dumpLayout("$indent  "))
            }
        }
    }
}

private val Double.padded
    get() = toString().padStart(4, ' ')