/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.ui.common.render.container
import hu.simplexion.adaptive.ui.common.render.layout

enum class AlignContent : AdaptiveInstruction {
    Start,
    Center,
    End;

    override fun apply(subject: Any) {
        container(subject) { it.alignContent = this }
    }
}

enum class AlignItems : AdaptiveInstruction {
    Center,
    Start,
    End;

    override fun apply(subject: Any) {
        container(subject) { it.alignItems = this }
    }
}

enum class AlignSelf : AdaptiveInstruction {
    Center,
    Start,
    End;

    override fun apply(subject: Any) {
        layout(subject) { it.alignSelf = this }
    }
}