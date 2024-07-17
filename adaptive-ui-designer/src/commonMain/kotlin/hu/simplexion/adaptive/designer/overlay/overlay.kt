package hu.simplexion.adaptive.designer.overlay

import hu.simplexion.adaptive.designer.overlay.model.containingFrame
import hu.simplexion.adaptive.designer.overlay.styles.containingBox
import hu.simplexion.adaptive.designer.utility.Selection
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ui.common.fragment.box

@Adaptive
fun overlay(selection: Selection) {
    val frame = containingFrame(selection)?.grow(1.0)

    if (frame != null) {
        box(*containingBox, frame) { }
    }
}
