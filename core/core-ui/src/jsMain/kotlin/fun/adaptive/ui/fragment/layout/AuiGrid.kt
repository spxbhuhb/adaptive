/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AuiBrowserAdapter
import `fun`.adaptive.ui.aui
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@AdaptiveActual(aui)
open class AuiGrid(
    adapter: AuiBrowserAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractGrid<HTMLElement, HTMLDivElement>(adapter, parent, declarationIndex) {

    /**
     * In browsers border is not counted when positioning absolutely.
     */
    override fun toFrameOffsets() : RawPosition {
        val padding = renderData.layout?.padding ?: RawSurrounding.ZERO

        return RawPosition(padding.top, padding.start)
    }

}