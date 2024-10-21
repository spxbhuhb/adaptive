/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.Name
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.utility.firstOrNullIfInstance

abstract class AbstractAuiFragment<RT>(
    adapter: AbstractAuiAdapter<RT, *>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AdaptiveFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    abstract val receiver: RT

    /**
     * Use this field when accessing actual UI specific adapter functions.
     */
    open val uiAdapter = adapter

    var previousRenderData = adapter.emptyRenderData

    /**
     * The initial render data is read from the adapter. This makes it possible to apply
     * global styling by fragment type.
     */
    var renderData = AuiRenderData(adapter)

    /**
     * Structural fragments (loop and select) set this to true to modify behaviour.
     */
    open val isStructural
        get() = false

    /**
     * When true, the fragment does **NOT** call the `addActual/removeActual` of its parent
     * but calls `addActualRoot/removeActualRoot` of the adapter directly.
     */
    open val isRootActual
        get() = false

    open val invalidInput : Boolean
        get() =  false

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) =
        Unit

    override fun genPatchInternal(): Boolean {
        patchInstructions()
        return true
    }

    open fun patchInstructions(addText: Boolean = false) {
        if (instructionIndex != - 1 && haveToPatch(dirtyMask, 1 shl instructionIndex)) {
            previousRenderData = renderData
            renderData = AuiRenderData(uiAdapter, previousRenderData, uiAdapter.themeFor(this), instructions)

            if (renderData.layout != previousRenderData.layout) {
                renderData.layoutFragment?.layoutChange(this)
            }

            if (addText && renderData.text == null) renderData.text = uiAdapter.defaultTextRenderData

            uiAdapter.applyRenderInstructions(this)
        }
    }

    override fun mount() {
        super.mount()

        val safeParent = parent

        if (isRootActual || safeParent == null) {
            adapter.addActualRoot(this)
        } else {
            safeParent.addActual(this, if (isStructural) null else true)
        }
    }

    override fun unmount() {
        val safeParent = parent

        if (isRootActual || safeParent == null) {
            adapter.removeActualRoot(this)
        } else {
            safeParent.removeActual(this, if (isStructural) null else true)
        }

        super.unmount()
    }

    /**
     * Basic layout computation that is used for intrinsic UI fragments. Layout fragments
     * override this method to implement their own calculation algorithm.
     */
    open fun computeLayout(proposedWidth: Double, proposedHeight: Double) {
        val data = renderData
        val layout = data.layout

        val instructedWidth = layout?.instructedWidth
        val innerWidth = data.innerWidth

        data.finalWidth = when {
            instructedWidth != null -> instructedWidth
            layout?.fillHorizontal == true -> proposedWidth
            innerWidth != null -> innerWidth + data.surroundingHorizontal
            proposedWidth.isFinite() -> proposedWidth
            else -> data.surroundingHorizontal
        }

        val instructedHeight = layout?.instructedHeight
        val innerHeight = data.innerHeight

        data.finalHeight = when {
            instructedHeight != null -> instructedHeight
            layout?.fillVertical == true -> proposedHeight
            innerHeight != null -> innerHeight + data.surroundingVertical
            proposedHeight.isFinite() -> proposedHeight
            else -> data.surroundingVertical
        }
    }

    open fun placeLayout(top: Double, left: Double) {
        val data = renderData

        data.finalTop = top
        data.finalLeft = left

        if (trace) trace("layout", "top: ${data.finalTop}, left: ${data.finalLeft}, width: ${data.finalWidth}, height: ${data.finalHeight}")

        uiAdapter.applyLayoutToActual(this)
    }

    private val Double.padded
        get() = toString().padStart(4, ' ')

    fun dumpLayout(indent: String): String {
        return buildString {
            val name = (indent + (instructions.firstOrNullIfInstance<Name>()?.name ?: this@AbstractAuiFragment::class.simpleName !!)).padEnd(40, ' ')
            val data = renderData

            appendLine("$name  top: ${data.finalTop.padded}    left: ${data.finalLeft.padded}    width: ${data.finalWidth.padded}    height: ${data.finalHeight.padded}")
            if (this@AbstractAuiFragment is AbstractContainer<*, *>) {
                layoutItems.forEach {
                    append(it.dumpLayout("$indent  "))
                }
            }
        }
    }
}