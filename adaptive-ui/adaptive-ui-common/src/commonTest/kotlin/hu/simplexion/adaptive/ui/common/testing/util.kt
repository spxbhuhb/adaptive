/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.testing

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.select.firstOrNullWith
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.Frame


inline fun <reified T : AdaptiveInstruction> AdaptiveAdapter.assertEquals(
    top: Float,
    left: Float,
    width: Float,
    height: Float
) {
    kotlin.test.assertEquals(Frame(top, left, width, height), (firstOrNullWith<T>() as AdaptiveUIFragment<*>).renderData.layoutFrame)
}