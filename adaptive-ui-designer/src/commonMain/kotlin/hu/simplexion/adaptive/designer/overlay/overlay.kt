package hu.simplexion.adaptive.designer.overlay

import hu.simplexion.adaptive.designer.overlay.styles.containingBox
import hu.simplexion.adaptive.designer.overlay.styles.guide
import hu.simplexion.adaptive.designer.utility.Selection
import hu.simplexion.adaptive.designer.utility.noHit
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ui.common.fragment.box
import hu.simplexion.adaptive.ui.common.instruction.dp
import hu.simplexion.adaptive.ui.common.instruction.maxHeight
import hu.simplexion.adaptive.ui.common.instruction.maxWidth
import hu.simplexion.adaptive.ui.common.instruction.position

@Adaptive
fun overlay(selection: Selection) {
    val frame = selection.containingFrame(selection)?.grow(1.0)

    // ----  rendering  ---------

    if (frame != null) {
        box(*containingBox, frame, noHit) { }
        box(*guide, position(frame.top, 0.dp), maxWidth) { }
        box(*guide, position(frame.top + frame.height - 1.0, 0.dp), maxWidth) { }
        box(*guide, position(0.dp, frame.left), maxHeight) { }
        box(*guide, position(0.dp, frame.left + frame.width - 1.0), maxHeight) { }
    }
}