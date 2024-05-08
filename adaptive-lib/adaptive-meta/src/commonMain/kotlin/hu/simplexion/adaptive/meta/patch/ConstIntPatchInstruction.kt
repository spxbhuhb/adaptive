/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.meta.patch

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.base.AdaptiveFragment
import hu.simplexion.adaptive.meta.AdaptivePatchInstructionImpl

@Adat
class ConstIntPatchInstructionData(
    val index: Int,
    val value : Int
)

class ConstIntPatchInstruction(
    val data : ConstIntPatchInstructionData
) : AdaptivePatchInstructionImpl {
    override fun patch(fragment: AdaptiveFragment<*>) {
        fragment.setStateVariable(data.index, data.value)
    }
}