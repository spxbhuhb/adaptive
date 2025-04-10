/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.instruction.DPixel

@Adat
class PopupAlign(
    val vertical: OuterAlignment?,
    val horizontal: OuterAlignment?,
    val absolute: Boolean = false,
    val modal: Boolean = false,
    val topMax: DPixel? = null
) : AdaptiveInstruction {

    fun flipVertical(): PopupAlign = PopupAlign(
        vertical = when (this.vertical) {
            OuterAlignment.Above -> OuterAlignment.Below
            OuterAlignment.Below -> OuterAlignment.Above
            OuterAlignment.Start -> OuterAlignment.End
            OuterAlignment.End -> OuterAlignment.Start
            else -> this.vertical
        },
        horizontal = this.horizontal
    )

    fun flipHorizontal(): PopupAlign = PopupAlign(
        vertical = this.vertical,
        horizontal = when (this.horizontal) {
            OuterAlignment.Before -> OuterAlignment.After
            OuterAlignment.After -> OuterAlignment.Before
            OuterAlignment.Start -> OuterAlignment.End
            OuterAlignment.End -> OuterAlignment.Start
            else -> this.horizontal
        }
    )

    fun flipBoth(): PopupAlign = flipVertical().flipHorizontal()

    @Suppress("unused")
    companion object {

        val beforeAbove = PopupAlign(horizontal = OuterAlignment.Before, vertical = OuterAlignment.Above)
        val beforeTop = PopupAlign(horizontal = OuterAlignment.Before, vertical = OuterAlignment.Start)
        val beforeCenter = PopupAlign(horizontal = OuterAlignment.Before, vertical = OuterAlignment.Center)
        val beforeBottom = PopupAlign(horizontal = OuterAlignment.Before, vertical = OuterAlignment.End)
        val beforeBelow = PopupAlign(horizontal = OuterAlignment.Before, vertical = OuterAlignment.Below)

        val afterAbove = PopupAlign(horizontal = OuterAlignment.After, vertical = OuterAlignment.Above)
        val afterTop = PopupAlign(horizontal = OuterAlignment.After, vertical = OuterAlignment.Start)
        val afterCenter = PopupAlign(horizontal = OuterAlignment.After, vertical = OuterAlignment.Center)
        val afterBottom = PopupAlign(horizontal = OuterAlignment.After, vertical = OuterAlignment.End)
        val afterBelow = PopupAlign(horizontal = OuterAlignment.After, vertical = OuterAlignment.Below)

        val aboveBefore = PopupAlign(vertical = OuterAlignment.Above, horizontal = OuterAlignment.Before)
        val aboveStart = PopupAlign(vertical = OuterAlignment.Above, horizontal = OuterAlignment.Start)
        val aboveCenter = PopupAlign(vertical = OuterAlignment.Above, horizontal = OuterAlignment.Center)
        val aboveEnd = PopupAlign(vertical = OuterAlignment.Above, horizontal = OuterAlignment.End)
        val aboveAfter = PopupAlign(vertical = OuterAlignment.Above, horizontal = OuterAlignment.After)

        val belowBefore = PopupAlign(vertical = OuterAlignment.Below, horizontal = OuterAlignment.Before)
        val belowStart = PopupAlign(vertical = OuterAlignment.Below, horizontal = OuterAlignment.Start)
        val belowCenter = PopupAlign(vertical = OuterAlignment.Below, horizontal = OuterAlignment.Center)
        val belowEnd = PopupAlign(vertical = OuterAlignment.Below, horizontal = OuterAlignment.End)
        val belowAfter = PopupAlign(vertical = OuterAlignment.Below, horizontal = OuterAlignment.After)

        val centerCenter = PopupAlign(vertical = OuterAlignment.Center, horizontal = OuterAlignment.Center)

        fun absoluteCenter(
            modal: Boolean = false,
            topMax: DPixel? = null
        ) = PopupAlign(
            vertical = OuterAlignment.Center,
            horizontal = OuterAlignment.Center,
            absolute = true,
            modal = modal,
            topMax = topMax
        )

        fun findBestPopupAlignment(preferred: PopupAlign, check: (PopupAlign) -> Boolean): PopupAlign {

            val attempts = listOf(
                preferred,
                preferred.flipVertical(),
                preferred.flipHorizontal(),
                preferred.flipBoth(),
                PopupAlign(horizontal = OuterAlignment.Center, vertical = OuterAlignment.Center)
            )

            return attempts.firstOrNull { check(it) } ?: preferred
        }
    }
}