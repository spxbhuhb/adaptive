/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.api.GroveRuntimeFragmentFactory
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.builtin.account_box
import `fun`.adaptive.ui.builtin.menu
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.utility.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun workspaceMain() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
        groveRuntimeCommon()

        commonMainStringsStringStore0.load()

        browser(CanvasFragmentFactory, SvgFragmentFactory, GroveRuntimeFragmentFactory, backend = backend { }) { adapter ->

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            val workspace = copyStore { WorkSpace() }

            grid {
                maxSize .. colTemplate(panelTheme.width, 1.fr, panelTheme.width)

                panelIcons(left = true, panels, panelTheme, workspace)

                grid {
                    maxSize
                    colTemplate(300.dp, 1.fr, 1.fr, 300.dp)
                    rowTemplate(1.fr, 1.fr, 300.dp)

                    workspaceSlot(panels.firstOrNull { it.uuid == workspace.leftTop }) .. gridPos(1,1)
                    workspaceSlot(panels.firstOrNull { it.uuid == workspace.leftMiddle }) .. gridPos(2,1)
                    workspaceSlot(panels.firstOrNull { it.uuid == workspace.leftBottom }) .. gridPos(3,1)

                    workspaceSlot(panels.firstOrNull { it.uuid == workspace.center }) .. gridPos(1,2, colSpan = 2, rowSpan = 2)

                    workspaceSlot(panels.firstOrNull { it.uuid == workspace.rightTop }) .. gridPos(1,4)
                    workspaceSlot(panels.firstOrNull { it.uuid == workspace.rightMiddle }) .. gridPos(2,4)
                    workspaceSlot(panels.firstOrNull { it.uuid == workspace.rightBottom }) .. gridPos(3,4)
                }

                panelIcons(left = false, panels, panelTheme, workspace)
            }
        }
    }
}

@Adaptive
fun workspaceSlot(panel: Panel?, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    box(instructions()) {
        maxSize
        if (panel != null) {
            panelContent(panel.fragmentFun)
        }
    }
    return fragment()
}

val panels = listOf(
    Panel(
        UUID(),
        "Palette",
        Graphics.menu,
        PanelPosition.LeftTop,
        ::palette,
        "⌘ P"
    ),
    Panel(
        UUID(),
        "Components",
        Graphics.account_box,
        PanelPosition.LeftTop,
        ::emptyPanel,
        "⌘ P"
    ),
    Panel(
        UUID(),
        "Palette",
        Graphics.menu,
        PanelPosition.LeftMiddle,
        ::emptyPanel
    ),
    Panel(
        UUID(),
        "Components",
        Graphics.account_box,
        PanelPosition.LeftMiddle,
        ::emptyPanel,
        "⌘ P"
    ),
    Panel(
        UUID(),
        "Palette",
        Graphics.menu,
        PanelPosition.LeftBottom,
        ::emptyPanel,
        "⌘ P"
    ),
    Panel(
        UUID(),
        "Components",
        Graphics.account_box,
        PanelPosition.LeftBottom,
        ::emptyPanel,
        "⌘ P"
    )
)

@Adaptive
fun panelContent(@Adaptive content: () -> AdaptiveFragment) {
    box {
        maxSize
        content()
    }
}

@Adat
class WorkSpace(
    val rightTop: UUID<Panel>? = null,
    val rightMiddle: UUID<Panel>? = null,
    val rightBottom: UUID<Panel>? = null,
    val center: UUID<Panel>? = null,
    val leftTop: UUID<Panel>? = null,
    val leftMiddle: UUID<Panel>? = null,
    val leftBottom: UUID<Panel>? = null
)

enum class PanelPosition {
    RightTop,
    RightMiddle,
    RightBottom,
    LeftTop,
    LeftMiddle,
    LeftBottom
}

class Panel(
    val uuid: UUID<Panel>,
    val name: String,
    val icon: GraphicsResourceSet,
    val position: PanelPosition,
    @Adaptive
    val fragmentFun: () -> AdaptiveFragment,
    val shortcut: String? = null
)

val panelTheme = PanelTheme()

class PanelTheme(
    val width: DPixel = 40.dp
) {
    val iconColumn = instructionsOf(
        maxHeight,
        width { width },
        spaceBetween
    )

    val rightIconColumn = iconColumn + borderLeft(colors.outline)
    val leftIconColumn = iconColumn + borderRight(colors.outline)

    val divider = instructionsOf(
        width { width },
        height { 16.dp },
        borderTop(colors.outline),
        margin(top = 8.dp, left = 8.dp, right = 8.dp, bottom = 7.dp)
    )

    val panelIconContainer = instructionsOf(
        size(width),
        margin { 6.dp },
        cornerRadius { 4.dp },
        alignItems.center
    )

    val panelIcon = instructionsOf(

    )

    val tooltipContainer = instructionsOf(
        paddingHorizontal { 16.dp },
        paddingVertical { 8.dp },
        backgrounds.reverse,
        cornerRadius { 4.dp },
        gap { 8.dp },
        zIndex { 100 }
    )

    val tooltipTextBase = instructionsOf(
        noSelect,
        textSmall
    )

    val tooltipName = tooltipTextBase + textColors.onReverse

    val tooltipShortcut = tooltipTextBase + textColors.onReverseVariant

}

@Adaptive
fun panelIcons(
    left: Boolean,
    panels: List<Panel>,
    theme: PanelTheme,
    workspace: WorkSpace
) {

    val top = panels.filter(if (left) PanelPosition.LeftTop else PanelPosition.RightTop)
    val middle = panels.filter(if (left) PanelPosition.LeftMiddle else PanelPosition.RightMiddle)
    val bottom = panels.filter(if (left) PanelPosition.LeftBottom else PanelPosition.RightBottom)

    column {
        if (left) theme.leftIconColumn else theme.rightIconColumn

        column {
            for (panel in top) {
                panelIcon(panel, theme, workspace)
            }
            if (top.isNotEmpty() && middle.isNotEmpty()) {
                box { theme.divider }
            }
            for (panel in middle) {
                panelIcon(panel, theme, workspace)
            }
        }

        column {
            for (panel in bottom) {
                panelIcon(panel, theme, workspace)
            }
        }
    }

}

fun List<Panel>.filter(position: PanelPosition) =
    filter { it.position == position }

@Adaptive
fun panelIcon(
    panel: Panel,
    theme: PanelTheme,
    workspace: WorkSpace
) {
    box {
        theme.panelIconContainer
        onClick { workspace.switchTo(panel) }

        icon(panel.icon)

        hoverPopup {
            popupAlign.afterCenter

            row {
                theme.tooltipContainer

                text(panel.name) .. theme.tooltipName

                if (panel.shortcut != null) {
                    text(panel.shortcut) .. theme.tooltipShortcut
                }
            }
        }
    }
}

fun WorkSpace.switchTo(panel: Panel) {
    when (panel.position) {
        PanelPosition.RightTop -> update(::rightTop, panel.uuid)
        PanelPosition.RightMiddle -> update(::rightMiddle, panel.uuid)
        PanelPosition.RightBottom -> update(::rightBottom, panel.uuid)
        PanelPosition.LeftTop -> update(::leftTop, panel.uuid)
        PanelPosition.LeftMiddle -> update(::leftMiddle, panel.uuid)
        PanelPosition.LeftBottom -> update(::leftBottom, panel.uuid)
    }
}

@Adaptive
fun emptyPanel(): AdaptiveFragment {
    box {
        padding { 16.dp }
        text("todo")
    }
    return fragment()
}

@Adaptive
fun palette() : AdaptiveFragment {
    box {
        padding { 16.dp }
        text("palette")
    }
    return fragment()
}