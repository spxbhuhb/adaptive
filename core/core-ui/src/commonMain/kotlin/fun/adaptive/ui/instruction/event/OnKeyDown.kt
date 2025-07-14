/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.event

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.render.event

@Adat
class OnKeyDown(
    override val handler: (event: UIEvent) -> Unit,
    override val feedbackText: String? = null,
    override val feedbackIcon: GraphicsResourceSet? = null,
    val key : String? = null
) : UIEventHandler {

    override fun applyTo(subject: Any) {
        event(subject) {
            it.additionalEvents = true
            it.onKeyDown = this
        }
    }

    override fun execute(event: UIEvent): Boolean {
        if (key == null || event.keyInfo?.key == key) {
            return super.execute(event)
        } else {
            return false
        }
    }
}