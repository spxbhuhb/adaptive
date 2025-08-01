package `fun`.adaptive.ui.api

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.resource.image.ImageResourceSet
import `fun`.adaptive.ui.aui
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend
import `fun`.adaptive.ui.fragment.layout.cell.CellBoxArrangement
import `fun`.adaptive.ui.fragment.layout.cell.CellDef
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphViewBackend
import `fun`.adaptive.ui.fragment.structural.PopupSourceViewBackend

@AdaptiveExpect(aui)
fun image(res: ImageResourceSet, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    manualImplementation(res, instructions)
}

@AdaptiveExpect(aui)
fun singleLineTextInput(
    value: String?,
    onChange: (newValue: String) -> Unit
): AdaptiveFragment {
    manualImplementation(value, onChange)
}

@AdaptiveExpect(aui)
fun multiLineTextInput(
    value: String?,
    onChange: (newValue: String) -> Unit,
): AdaptiveFragment {
    manualImplementation(value, onChange)
}

@AdaptiveExpect(aui)
fun box(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit): AdaptiveFragment {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(aui)
fun flowBox(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit): AdaptiveFragment {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(aui)
fun rectangle(vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    manualImplementation(instructions)
}

/**
 * Create a box added to the root fragment list of the adapter. The box is automatically placed
 * at position (0,0) and sized to fill the all the viewport.
 */
@AdaptiveExpect(aui)
fun rootBox(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit): AdaptiveFragment {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(aui)
fun row(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit): AdaptiveFragment {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(aui)
fun cellBox(
    cells : List<CellDef>? = null,
    arrangement: CellBoxArrangement? = null,
    vararg instructions: AdaptiveInstruction,
    @Adaptive content: () -> Unit
): AdaptiveFragment {
    manualImplementation(arrangement, cells, instructions, content)
}


@AdaptiveExpect(aui)
fun column(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit): AdaptiveFragment {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(aui)
fun grid(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit): AdaptiveFragment {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(aui)
fun space(vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    manualImplementation(instructions)
}

@AdaptiveExpect(aui)
fun text(content: Any?, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    manualImplementation(content, instructions)
}

@AdaptiveExpect(aui)
fun flowText(content: Any?, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    manualImplementation(content, instructions)
}

@AdaptiveExpect(aui)
fun draggable(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit): AdaptiveFragment {
    manualImplementation(content, instructions)
}

@AdaptiveExpect(aui)
fun dropTarget(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit): AdaptiveFragment {
    manualImplementation(content, instructions)
}

@AdaptiveExpect(aui)
fun hoverPopup(vararg instructions: AdaptiveInstruction, @Adaptive content: (hide: () -> Unit) -> Unit): AdaptiveFragment {
    manualImplementation(content, instructions)
}

@AdaptiveExpect(aui)
fun primaryPopup(sourceViewBackend: PopupSourceViewBackend? = null, vararg instructions: AdaptiveInstruction, @Adaptive content: (hide: () -> Unit) -> Unit): AdaptiveFragment {
    manualImplementation(sourceViewBackend, content, instructions)
}

@AdaptiveExpect(aui)
fun contextPopup(sourceViewBackend: PopupSourceViewBackend? = null, vararg instructions: AdaptiveInstruction, @Adaptive content: (hide: () -> Unit) -> Unit): AdaptiveFragment {
    manualImplementation(sourceViewBackend, content, instructions)
}

@AdaptiveExpect(aui)
fun splitPane(
    viewBackend: SplitPaneViewBackend,
    @Adaptive pane1: () -> Unit,
    @Adaptive divider: () -> Unit,
    @Adaptive pane2: () -> Unit,
    onChange: ((Double) -> Unit)? = null,
    vararg instructions: AdaptiveInstruction
): AdaptiveFragment {
    manualImplementation(viewBackend, pane1, divider, pane2, onChange, instructions)
}

@AdaptiveExpect(aui)
fun paragraph(viewBackend: ParagraphViewBackend): AdaptiveFragment {
    manualImplementation(viewBackend)
}

@AdaptiveExpect(aui)
fun afterPatchBatch(once : Boolean = true, task: (declaringFragment : AdaptiveFragment) -> Unit) {
    manualImplementation(once, task)
}