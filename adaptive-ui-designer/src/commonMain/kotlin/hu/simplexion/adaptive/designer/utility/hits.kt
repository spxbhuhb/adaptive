package hu.simplexion.adaptive.designer.utility

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.fragment.layout.AbstractContainer

val noHit = object : AdaptiveInstruction {}

/**
 * Finds layout items in the container that have [x] and [y] in their frame. [x] and [y]
 * are relative to the container.
 *
 * @return  All the items which are hit. If one item contains another, both are added to the result.
 *          Empty list if the fragment is not a container
 */
fun hits(
    container: AbstractCommonFragment<*>,
    x: Double,
    y: Double
): List<AbstractCommonFragment<*>> {

    if (container !is AbstractContainer<*, *>) return emptyList()

    val result = mutableListOf<AbstractCommonFragment<*>>()

    for (item in container.layoutItems) {
        val renderData = item.renderData

        val top = renderData.finalTop
        if (y < top) continue

        val left = renderData.finalLeft
        if (x < left) continue

        val right = left + renderData.finalWidth
        if (x >= right) continue

        val bottom = top + renderData.finalHeight
        if (y >= bottom) continue

        if (noHit !in item.instructions) {
            result += item
        }

        if (item is AbstractContainer<*, *>) {
            result += hits(item, x - left, y - top)
        }
    }

    return result
}