/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.testing.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.testing.AuiTestAdapter
import `fun`.adaptive.ui.testing.TestReceiver
import `fun`.adaptive.utility.checkIfInstance

@AdaptiveActual("test")
open class AdaptiveImage(
    adapter: AuiTestAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<TestReceiver>(adapter, parent, index, 1, stateSize()) {

    override val receiver = TestReceiver()

    @Suppress("unused")
    private val res: GraphicsResourceSet
        by stateVariable()

    val fakeInstructions: AdaptiveInstructionGroup
        by stateVariable()

    override fun auiPatchInternal() = Unit

}