package `fun`.adaptive.grove.designer.overlay

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.designer.overlay.styles.containingBox
import `fun`.adaptive.grove.designer.overlay.styles.guide
import `fun`.adaptive.grove.designer.overlay.styles.targetBox
import `fun`.adaptive.grove.designer.utility.Selection
import `fun`.adaptive.grove.designer.utility.noHit
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.instruction.layout.Frame

@Adaptive
fun overlay(selection: Selection, target: Selection) {
    val targetFrame = targetFrame(selection, target)
    val frame = selection.containingFrame(selection)?.grow(1.0)

    // ----  rendering  ---------

    if (frame != null) {
        box(*containingBox, frame, noHit) { }
        box(*guide, position(frame.top, 0.dp), maxWidth) { }
        box(*guide, position(frame.top + frame.height - 1.0, 0.dp), maxWidth) { }
        box(*guide, position(0.dp, frame.left), maxHeight) { }
        box(*guide, position(0.dp, frame.left + frame.width - 1.0), maxHeight) { }
    }

    if (targetFrame != null) {
        box(*targetBox, targetFrame, noHit) { }
    }
}

fun targetFrame(selection: Selection, target: Selection): Frame? {
    if (target.isEmpty()) return null

    val container = target.items.lastOrNull { it is AbstractContainer<*, *> } ?: return null
    if (container in selection) return null

    val renderData = container.renderData
    val uiAdapter = container.uiAdapter

    return Frame(
        uiAdapter.toDp(renderData.finalTop),
        uiAdapter.toDp(renderData.finalLeft),
        uiAdapter.toDp(renderData.finalWidth),
        uiAdapter.toDp(renderData.finalHeight)
    )
}