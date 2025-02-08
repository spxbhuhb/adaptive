package `fun`.adaptive.grove.sheet.fragment

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.value.adaptiveValue
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.sheet.model.SheetSelection.Companion.emptySelection
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.grove.sheet.operation.*
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.event.Keys
import `fun`.adaptive.ui.instruction.event.UIEvent
import `fun`.adaptive.ui.instruction.layout.Frame
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.utility.vmNowMicro
import kotlin.math.abs

@Adaptive
fun controlLayer(viewModel: SheetViewModel) {

    var moveStart = 0L
    var startPosition = Position.NaP
    var lastPosition = Position.NaP
    val selection = adaptiveValue { viewModel.selectionStore } ?: emptySelection
    var controlFrame = selection.containingFrame.toFrame(adapter()).grow(8.0)

    dropTarget {

        onDrop(focusOnDrop = true) { event ->
            val template = (event.transferData?.data as? LfmDescendant) ?: return@onDrop
            val position = event.position
            viewModel += Add(position.left, position.top, template)
        }

        box {
            maxSize .. tabIndex { 0 }

            onPrimaryDown { event ->
                startPosition = event.position
                lastPosition = startPosition

                moveStart = vmNowMicro()

                if (lastPosition !in controlFrame) {
                    viewModel.select(event.x, event.y, EventModifier.SHIFT in event)
                }
            }

            onMove { event ->
                // when start position is NaP the pointer movement started outside the window
                if (lastPosition === Position.NaP) return@onMove
                if (startPosition === Position.NaP) return@onMove

                val newPosition = event.position

                if (selection.isEmpty()) {
                    val dx = abs(startPosition.left.value - newPosition.left.value)
                    val dy = abs(startPosition.top.value - newPosition.top.value)

                    if (dx >= 1.0 && dy >= 1.0) {
                        controlFrame = Frame(startPosition, newPosition)
                    } else {
                        controlFrame = Frame.NaF
                    }

                } else {
                    viewModel += Move(moveStart, newPosition.left - lastPosition.left, newPosition.top - lastPosition.top)
                }

                lastPosition = newPosition
            }

            onPrimaryUp { event ->
                // when start position is NaP the pointer movement started outside the window
                if (selection.isEmpty() && startPosition !== Position.NaP) {
                    controlFrame = Frame.NaF
                    viewModel.select(startPosition.toRaw(adapter()), event.x, event.y, EventModifier.SHIFT in event)
                }

                startPosition = Position.NaP
                lastPosition = Position.NaP
                moveStart = 0L
            }

            onKeydown { event ->
                keyDownHandler(event, selection, viewModel)
            }

            if (controlFrame != Frame.NaF) {
                controls(controlFrame)
            }
        }
    }
}

private fun keyDownHandler(event: UIEvent, selection: SheetSelection, viewModel: SheetViewModel) {
    when (event.keyInfo?.key) {
        Keys.ESCAPE -> viewModel.select()

        Keys.X -> viewModel += Cut()

        Keys.C -> viewModel += Copy()

        Keys.V -> viewModel += Paste()

        Keys.Z -> if (EventModifier.CTRL in event.modifiers || EventModifier.META in event.modifiers) {
            if (EventModifier.SHIFT in event.modifiers) {
                viewModel += Redo()
            } else {
                viewModel += Undo()
            }
        }

        Keys.BACKSPACE -> viewModel += Remove()
        Keys.DELETE -> viewModel += Remove()

        Keys.ARROW_UP -> move(selection, viewModel, 0.0, - 1.0)
        Keys.ARROW_DOWN -> move(selection, viewModel, 0.0, 1.0)
        Keys.ARROW_LEFT -> move(selection, viewModel, - 1.0, 0.0)
        Keys.ARROW_RIGHT -> move(selection, viewModel, 1.0, 0.0)
    }
}

private fun move(selection: SheetSelection, viewModel: SheetViewModel, deltaX: Double, deltaY: Double) {
    if (! selection.isEmpty()) viewModel += Move(vmNowMicro(), deltaX.dp, deltaY.dp)
}

@Adaptive
private fun controls(frame: Frame) {
    box {
        frame

        // borders

        box { ControlStyles.topBorder }
        box { ControlStyles.endBorder }
        box { ControlStyles.bottomBorder }
        box { ControlStyles.startBorder }

        // resize handles

        box { ControlStyles.startTopHandle }
        box { ControlStyles.startCenterHandle }
        box { ControlStyles.startBottomHandle }

        box { ControlStyles.topCenterHandle }
        box { ControlStyles.bottomCenterHandle }

        box { ControlStyles.endTopHandle }
        box { ControlStyles.endCenterHandle }
        box { ControlStyles.endBottomHandle }
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


