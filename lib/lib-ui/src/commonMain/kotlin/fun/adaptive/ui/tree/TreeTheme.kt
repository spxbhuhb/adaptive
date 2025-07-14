package `fun`.adaptive.ui.tree

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.arrow_drop_down
import `fun`.adaptive.ui.generated.resources.arrow_right
import `fun`.adaptive.ui.generated.resources.chevron_right
import `fun`.adaptive.ui.generated.resources.keyboard_arrow_down
import `fun`.adaptive.ui.generated.resources.keyboard_arrow_right
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.SizeBase
import `fun`.adaptive.ui.instruction.layout.SizeStrategy
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.background
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.foreground

open class TreeTheme(
    open val itemHeight: DPixel = 26.dp,
    open val indent: DPixel = 22.dp,
    open val itemPadding : DPixel = 4.dp,
) {
    open val container = instructionsOf(
        tabIndex { -1 },
        alignItems.startCenter,
        fillStrategy.resizeToMax,
    )

    open val icon = instructionsOf(
        size(18.dp),
        svgWidth(18.dp),
        svgHeight(18.dp)
    )

    open val item = instructionsOf(
        height(itemHeight),
        alignItems.startCenter,
        cornerRadius { 4.dp },
        SizeStrategy(horizontalBase = SizeBase.Larger),
    )

    open val label = instructionsOf(
        fontSize(13.sp),
        lineHeight(18.dp),
        paddingLeft(itemPadding),
        normalFont,
        noSelect
    )

    open fun itemBackground(selected: Boolean, focus: Boolean, hover : Boolean) =
        background(selected, focus = focus, variant = true, hover = hover)

    open fun itemForeground(selected: Boolean, focus: Boolean, hover : Boolean) =
        foreground(selected, focus = focus, hover = hover)

    open val handleContainerFront = instructionsOf(
        size(24.dp, 24.dp),
        alignItems.center
    )

    open val handleContainerEnd = instructionsOf(
        size(24.dp, 24.dp),
        alignItems.center,
        alignSelf.end
    )

    open val handleIcon = instructionsOf(
        size(20.dp),
        svgWidth(20.dp),
        svgHeight(20.dp)
    )

    open val handleIconOpen = Graphics.keyboard_arrow_down

    open val handleIconClosed = Graphics.keyboard_arrow_right

    open fun itemHandleColors(selected : Boolean, focus: Boolean) =
        fill(foreground(selected, focus = focus).color)

    companion object {
        var DEFAULT = TreeTheme()
        var list = TreeTheme(indent = 0.dp, itemPadding = 8.dp)
    }
}