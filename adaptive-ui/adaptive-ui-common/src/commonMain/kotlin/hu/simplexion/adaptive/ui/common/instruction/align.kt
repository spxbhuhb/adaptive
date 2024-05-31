/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.adapter.RenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

enum class AlignContent : AdaptiveInstruction {
    Start,
    Center,
    End;

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.alignContent = this }
    }
}

enum class AlignItems : AdaptiveInstruction {
    Center,
    Start,
    End;

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.alignItems = this }
    }
}

enum class AlignSelf : AdaptiveInstruction {
    Center,
    Start,
    End;

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> { it.alignSelf = this }
    }
}