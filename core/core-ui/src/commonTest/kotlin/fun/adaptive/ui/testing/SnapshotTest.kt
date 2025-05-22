/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.testing

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.query.firstOrNull
import `fun`.adaptive.foundation.visitor.InstructionReplaceTransform
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.ui.support.snapshot.FragmentSnapshot
import `fun`.adaptive.ui.support.snapshot.uiContainerSnapshot
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SnapshotTest(
    val adapter: AuiTestAdapter,
    val testWidth: Double,
    val testHeight: Double
) {
    fun snapshot() = FragmentSnapshot(
        "<root>",
        null,
        emptyList(),
        adapter.rootFragment.children.filterIsInstance<AbstractContainer<*, *>>().map { it.uiContainerSnapshot() },
        null, null, null, null
    )

    fun printLayout() {
        val data = SnapshotLayoutDumpVisitor.VisitorData()
        snapshot().accept(SnapshotLayoutDumpVisitor(), data)
        println(data.output)
    }

    fun replace(old: AdaptiveInstruction, new: AdaptiveInstruction) {
        adapter.rootFragment.transformChildren(InstructionReplaceTransform(old, new), null)
        adapter.closePatchBatch()
    }

    fun getUiFragment(fragmentName: String): AbstractAuiFragment<*> {
        val inst = name(fragmentName)

        val fragment : AdaptiveFragment? = adapter.firstOrNull { inst in it.instructions }
        assertNotNull(fragment, "Fragment with name $fragmentName was not found")

        assertIs<AbstractAuiFragment<*>>(fragment, "Fragment $fragment is not a AbstractAuiFragment")

        return fragment
    }

    fun getRenderData(fragmentName: String): AuiRenderData =
        getUiFragment(fragmentName).renderData

    fun assertNotExist(fragmentName: String) {
        assertNull(adapter.firstOrNull { name(fragmentName) in it.instructions }, "Fragment with name $fragmentName was found")
    }

    fun assertLayoutParentFinal(fragmentName: String, top: Number, left: Number, width: Number, height: Number) {
        val renderData = getRenderData(fragmentName).layoutFragment?.renderData
        assertNotNull(renderData, "RenderData for parent of $fragmentName was not found")
        assertFinal(renderData, top.toDouble(), left.toDouble(), width.toDouble(), height.toDouble())
    }

    fun assertFinal(fragmentName: String, top: Number, left: Number, width: Number, height: Number) {
        assertFinal(getRenderData(fragmentName), top.toDouble(), left.toDouble(), width.toDouble(), height.toDouble())
    }

    fun assertFinal(renderData: AuiRenderData, top: Number, left: Number, width: Number, height: Number) {
        assertEquals(top.toDouble(), renderData.finalTop, "Top should be $top")
        assertEquals(left.toDouble(), renderData.finalLeft, "Left should be $left")
        assertEquals(width.toDouble(), renderData.finalWidth, "Width should be $width")
        assertEquals(height.toDouble(), renderData.finalHeight, "Height should be $height")
    }
}