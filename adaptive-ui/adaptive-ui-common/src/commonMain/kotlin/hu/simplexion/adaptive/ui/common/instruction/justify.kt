/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.render.CommonRenderData
import hu.simplexion.adaptive.ui.common.render.container
import hu.simplexion.adaptive.ui.common.render.layout
import hu.simplexion.adaptive.utility.alsoIfInstance

enum class JustifyContent : AdaptiveInstruction {
    Start,
    Center,
    End;

    override fun apply(subject: Any) {
        container(subject) { it.justifyContent = this }
    }
}

enum class JustifyItems : AdaptiveInstruction {
    Center,
    Start,
    End;

    override fun apply(subject: Any) {
        container(subject) { it.justifyItems = this }
    }
}

enum class JustifySelf : AdaptiveInstruction {
    Center,
    Start,
    End;

    override fun apply(subject: Any) {
        layout(subject) { it.justifySelf = this }
    }
}