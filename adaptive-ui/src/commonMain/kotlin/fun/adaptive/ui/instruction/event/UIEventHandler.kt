/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.event

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction

interface UIEventHandler : AdaptiveInstruction {

    val handler: (event: UIEvent) -> Unit

    fun execute(event: UIEvent) {
        handler(event)
        event.patchIfDirty()
    }
}