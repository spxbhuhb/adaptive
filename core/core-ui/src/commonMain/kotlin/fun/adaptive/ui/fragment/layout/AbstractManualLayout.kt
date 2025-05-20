/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment

abstract class AbstractManualLayout<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    stateSize: Int = 2
) : AbstractContainer<RT, CRT>(
    adapter, parent, declarationIndex, stateSize
) {

    override var isRootActual: Boolean = false

    var computeLayoutFun: ((proposedWidth: Double, proposedHeight: Double) -> Unit)? = null

    var updateLayoutFun: ((updateId: Long, item: AbstractAuiFragment<*>?) -> Unit)? = null

    override fun computeLayout(
        proposal: SizingProposal
    ) {
        computeLayoutFun?.invoke(proposal.containerWidth, proposal.containerWidth)
    }

    override fun updateLayout(updateId: Long, item: AbstractAuiFragment<*>?) {
        val safeUpdateLayoutFun = updateLayoutFun

        if (safeUpdateLayoutFun == null) {
            super.updateLayout(updateId, item)
        } else {
            safeUpdateLayoutFun(updateId, item)
        }
    }

}