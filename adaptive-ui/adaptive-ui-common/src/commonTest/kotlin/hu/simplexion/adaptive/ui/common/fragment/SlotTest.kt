/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.fragment.FoundationDelegate
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.foundation.query.first
import hu.simplexion.adaptive.foundation.query.firstOrNull
import hu.simplexion.adaptive.foundation.testing.AdaptiveT0
import hu.simplexion.adaptive.foundation.testing.AdaptiveT1
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.fragment.structural.CommonSlot
import hu.simplexion.adaptive.ui.common.instruction.HistorySize
import hu.simplexion.adaptive.ui.common.testing.CommonTestAdapter
import kotlin.test.*

class SlotTest {

    val SLOT = 0
    val T0 = 1
    val T12 = 2
    val T23 = 3
    val T45 = 4
    val T56 = 5

    @Test
    fun testHistoryBasic() {
        val adapter = CommonTestAdapter()
        val slot = setup(adapter)
        val delegate = slot.parent !!

        val t0 = slot.firstOrNull<AdaptiveT0>(true)
        assertNotNull(t0)
        assertTrue(slot.backHistory.isEmpty())
        assertTrue(slot.forwardHistory.isEmpty())

        slot.setContent(delegate, 2)

        var content: AdaptiveFragment? = slot.firstOrNull<AdaptiveT0>(true)
        assertNull(content)

        val t12 = slot.firstOrNull<AdaptiveT1>(true)
        assertNotNull(t12)
        assertEquals(12, t12.state[0])

        assertEquals(1, slot.backHistory.size)
        assertEquals(t0, slot.backHistory[0][0])
        assertTrue(slot.forwardHistory.isEmpty())

        slot.setContent(delegate, 3)

        content = slot.firstOrNull { it is AdaptiveT1 && it.state[0] == 12 }
        assertNull(content)

        val t23 = slot.firstOrNull<AdaptiveT1>(true)
        assertNotNull(t23)
        assertEquals(23, t23.state[0])

        assertEquals(2, slot.backHistory.size)
        assertEquals(t0, slot.backHistory[0][0])
        assertEquals(t12, slot.backHistory[1][0])
        assertTrue(slot.forwardHistory.isEmpty())
    }

    @Test
    fun testBackAndForward() {
        val adapter = CommonTestAdapter()
        val slot = setup(adapter)
        val delegate = slot.parent !!

        val t0 = slot.first<AdaptiveT0>(true)
        val t12 = slot.setContent(delegate, 2)
        val t23 = slot.setContent(delegate, 3)

        with(slot) {
            assertState(listOf(t0, t12), t23, listOf())

            back()
            assertState(listOf(t0), t12, listOf(t23))

            back()
            assertState(listOf(), t0, listOf(t12, t23))

            back() // back is no-op when back history is empty
            assertState(listOf(), t0, listOf(t12, t23))

            forward()
            assertState(listOf(t0), t12, listOf(t23))

            forward()
            assertState(listOf(t0, t12), t23, listOf())

            forward() // forward is no-op when forward history is empty
            assertState(listOf(t0, t12), t23, listOf())

            back()
            assertState(listOf(t0), t12, listOf(t23))

            val t45 = setContent(delegate, T45) // setting content clears forward
            assertState(listOf(t0, t12), t45, listOf())

            val t56 = setContent(delegate, T56) // backward history is cut to max size
            assertState(listOf(t12, t45), t56, listOf())

            // I don't think it is possible to overextend forward history
        }
    }

    fun CommonSlot.assertState(
        backHistory: List<AdaptiveFragment>,
        current: AdaptiveFragment,
        forwardHistory: List<AdaptiveFragment>
    ) {
        assertEquals(backHistory.size, this.backHistory.size)
        backHistory.forEachIndexed { index, fragment ->
            assertEquals(fragment, this.backHistory[index][0])
        }

        assertEquals(1, this.children.size)
        assertEquals(current, this.children[0])

        assertEquals(forwardHistory.size, this.forwardHistory.size)
        forwardHistory.forEachIndexed { index, fragment ->
            assertEquals(fragment, this.forwardHistory[index][0])
        }
    }

    fun setup(adapter: CommonTestAdapter): CommonSlot {
        FoundationDelegate(
            adapter, null, 0,

            buildFun = { parent, index ->
                when (index) {
                    SLOT -> CommonSlot(parent.adapter as AbstractCommonAdapter<*, *>, parent, index)
                    T0 -> AdaptiveT0(parent.adapter, parent, index)
                    T12 -> AdaptiveT1(parent.adapter, parent, index)
                    T23 -> AdaptiveT1(parent.adapter, parent, index)
                    T45 -> AdaptiveT1(parent.adapter, parent, index)
                    T56 -> AdaptiveT1(parent.adapter, parent, index)
                    else -> invalidIndex(index)
                }
            },

            patchDescendantFun = {
                when (it.declarationIndex) {
                    SLOT -> {
                        it.setStateVariable(0, arrayOf(HistorySize(2)))
                        it.setStateVariable(1, BoundFragmentFactory(this, T0)) // initialContent
                    }

                    T0 -> Unit
                    T12 -> it.setStateVariable(0, 12)
                    T23 -> it.setStateVariable(0, 23)
                    T45 -> it.setStateVariable(0, 45)
                    T56 -> it.setStateVariable(0, 56)
                }
            }

        ).apply {
            adapter.rootFragment = this

            this.create()
            this.mount()

        }

        return adapter.first<CommonSlot>()
    }

}

