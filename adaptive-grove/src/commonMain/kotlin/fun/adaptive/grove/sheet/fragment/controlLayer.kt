package `fun`.adaptive.grove.sheet.fragment

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.Handles
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.operation.Add
import `fun`.adaptive.grove.sheet.operation.AddModel
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.layout.Frame
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun controlLayer(controller: SheetViewController) {

    val controlFrame = valueFrom { controller.controlFrameStore } ?: Frame.NaF

    dropTarget {

        onDrop(focusOnDrop = true) { event ->
            val model = (event.transferData?.data as? LfmDescendant) ?: return@onDrop
            val position = event.position
            controller += AddModel(model)
            controller += Add(position.left, position.top, model.uuid)
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

        box { ControlStyles.topBorder } .. onPrimaryDown { controller.activeHandle = Handles.TOP_BORDER }
        box { ControlStyles.endBorder } .. onPrimaryDown { controller.activeHandle = Handles.RIGHT_BORDER }
        box { ControlStyles.bottomBorder } .. onPrimaryDown { controller.activeHandle = Handles.BOTTOM_BORDER }
        box { ControlStyles.startBorder } .. onPrimaryDown { controller.activeHandle = Handles.LEFT_BORDER }

        // resize handles

        box { ControlStyles.startTopHandle } .. onPrimaryDown { controller.activeHandle = Handles.LEFT_TOP }
        box { ControlStyles.startCenterHandle } .. onPrimaryDown { controller.activeHandle = Handles.LEFT_CENTER }
        box { ControlStyles.startBottomHandle } .. onPrimaryDown { controller.activeHandle = Handles.LEFT_BOTTOM }

        box { ControlStyles.topCenterHandle } .. onPrimaryDown { controller.activeHandle = Handles.TOP_CENTER }
        box { ControlStyles.bottomCenterHandle } .. onPrimaryDown { controller.activeHandle = Handles.BOTTOM_CENTER }

        box { ControlStyles.endTopHandle } .. onPrimaryDown { controller.activeHandle = Handles.RIGHT_TOP }
        box { ControlStyles.endCenterHandle } .. onPrimaryDown { controller.activeHandle = Handles.RIGHT_CENTER }
        box { ControlStyles.endBottomHandle } .. onPrimaryDown { controller.activeHandle = Handles.RIGHT_BOTTOM }
    }
}

private object ControlStyles {

    val controlWidth = 8.dp
    val borderBackground = backgroundColor { colors.onSurfaceFriendly.opaque(0.2f) }

    val resizeHandle = size(8.dp, 8.dp) .. backgrounds.friendly .. border(colors.onSurfaceFriendly, 1.dp)

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


