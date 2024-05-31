/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.adapter.RenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

class AdaptiveUIEvent(
    val fragment: AdaptiveUIFragment<*>,
    val nativeEvent : Any?
)

fun onClick(handler : (event : AdaptiveUIEvent) -> Unit) = OnClick(handler)

class OnClick(val handler : (event : AdaptiveUIEvent) -> Unit) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.onClick = this }
    }

    override fun toString(): String {
        return "OnClick"
    }
}