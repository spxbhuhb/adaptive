package `fun`.adaptive.grove.sheet.fragment

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.adaptiveValue
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.ControlNames
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.operation.Add
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.layout.Frame
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun controlLayer(controller: SheetViewController) {

    val controlFrame = adaptiveValue { controller.controlFrameStore } ?: Frame.NaF

    dropTarget {

        onDrop(focusOnDrop = true) { event ->
            val template = (event.transferData?.data as? LfmDescendant) ?: return@onDrop
            val position = event.position
            controller += Add(position.left, position.top, template)
        }

        box {
            maxSize .. tabIndex { 0 }

            onPrimaryDown {
                controller.onTransformStart(it.position, EventModifier.SHIFT in it)
            }

            onMove {
                controller.onTransformChange(it.position)
            }

            onPrimaryUp {
                controller.onTransformEnd(it.position, EventModifier.SHIFT in it)
            }

            onKeydown { event ->
                controller.onKeyDown(event.keyInfo ?: return@onKeydown, event.modifiers)
            }

            if (controlFrame != Frame.NaF) {
                controls(controlFrame, controller)
            }
        }
    }
}

@Adaptive
private fun controls(frame: Frame, controller: SheetViewController) {
    box {
        frame

        // borders

        box { ControlStyles.topBorder } .. onPrimaryDown { controller.activeControl = ControlNames.TOP_BORDER }
        box { ControlStyles.endBorder } .. onPrimaryDown { controller.activeControl = ControlNames.RIGHT_BORDER }
        box { ControlStyles.bottomBorder } .. onPrimaryDown { controller.activeControl = ControlNames.BOTTOM_BORDER }
        box { ControlStyles.startBorder } .. onPrimaryDown { controller.activeControl = ControlNames.LEFT_BORDER }

        // resize handles

        box { ControlStyles.startTopHandle } .. onPrimaryDown { controller.activeControl = ControlNames.LEFT_TOP }
        box { ControlStyles.startCenterHandle } .. onPrimaryDown { controller.activeControl = ControlNames.LEFT_CENTER }
        box { ControlStyles.startBottomHandle } .. onPrimaryDown { controller.activeControl = ControlNames.LEFT_BOTTOM }

        box { ControlStyles.topCenterHandle } .. onPrimaryDown { controller.activeControl = ControlNames.TOP_CENTER }
        box { ControlStyles.bottomCenterHandle } .. onPrimaryDown { controller.activeControl = ControlNames.BOTTOM_CENTER }

        box { ControlStyles.endTopHandle } .. onPrimaryDown { controller.activeControl = ControlNames.RIGHT_TOP }
        box { ControlStyles.endCenterHandle } .. onPrimaryDown { controller.activeControl = ControlNames.RIGHT_CENTER }
        box { ControlStyles.endBottomHandle } .. onPrimaryDown { controller.activeControl = ControlNames.RIGHT_BOTTOM }
    }
}

private object ControlStyles {

    val controlWidth = 8.dp
    val borderBackground = backgroundColor { colors.outline.opaque(0.2f) }

    val resizeHandle = size(8.dp, 8.dp) .. backgrounds.overlay .. borders.outline

    val topBorder = borderBackground .. alignSelf.top .. maxWidth .. height(controlWidth)
    val startBorder = borderBackground .. alignSelf.startTop .. maxHeight .. width(controlWidth)
    val bottomBorder = borderBackground .. alignSelf.bottom .. maxWidth .. height(controlWidth)
    val endBorder = borderBackground .. alignSelf.endTop .. maxHeight .. width(controlWidth)

    val startTopHandle = resizeHandle .. alignSelf.startTop
    val startCenterHandle = resizeHandle .. alignSelf.startCenter
    val startBottomHandle = resizeHandle .. alignSelf.startBottom

    val topCenterHandle = resizeHandle .. alignSelf.topCenter
    val bottomCenterHandle = resizeHandle .. alignSelf.bottomCenter

    val endTopHandle = resizeHandle .. alignSelf.endTop
    val endCenterHandle = resizeHandle .. alignSelf.endCenter
    val endBottomHandle = resizeHandle .. alignSelf.endBottom
}


