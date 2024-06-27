/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.support.layout.AbstractGrid
import hu.simplexion.adaptive.ui.common.support.layout.RawPosition
import hu.simplexion.adaptive.ui.common.support.layout.RawSurrounding
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@AdaptiveActual(common)
open class CommonGrid(
    adapter: CommonAdapter,
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