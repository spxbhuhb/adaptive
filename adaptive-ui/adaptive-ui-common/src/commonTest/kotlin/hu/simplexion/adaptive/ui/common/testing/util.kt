/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.testing

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.query.firstOrNullWith
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.support.RawFrame


inline fun <reified T : AdaptiveInstruction> AdaptiveAdapter.assertEquals(
    top: Int,
    left: Int,
    width: Int,
    height: Int
) {
    kotlin.test.assertEquals(
        RawFrame(top.toFloat(), left.toFloat(), width.toFloat(), height.toFloat()),
        (firstOrNullWith<T>() as AbstractCommonFragment<*>).layoutFrame
    )
}