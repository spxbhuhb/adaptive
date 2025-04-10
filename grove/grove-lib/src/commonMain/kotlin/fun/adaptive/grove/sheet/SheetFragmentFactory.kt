package `fun`.adaptive.grove.sheet

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.grove.sheet.fragment.GroveDrawingLayer
import `fun`.adaptive.ui.AbstractAuiAdapter

object SheetFragmentFactory : FoundationFragmentFactory() {
    init {
        add("grove:drawinglayer") { p, i, s -> GroveDrawingLayer(p.adapter as AbstractAuiAdapter<*,*>, p, i) }
    }
}