/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.event

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.resource.graphics.GraphicsResourceSet

interface UIEventHandler : AdaptiveInstruction {

    val handler: (event: UIEvent) -> Unit

    val feedbackText: String?
        get() = null

    val feedbackIcon: GraphicsResourceSet?
        get() = null

    /**
     * Execute [handler] the event and patch the fragment if dirty.
     *
     * @return  true if the event handler has been executed, false otherwise
     */
    fun execute(event: UIEvent): Boolean {
        handler(event)
        event.patchIfDirty()
        return true
    }
}