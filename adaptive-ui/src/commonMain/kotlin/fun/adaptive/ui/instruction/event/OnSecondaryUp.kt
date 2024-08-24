/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.event

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.render.event
import `fun`.adaptive.ui.render.model.EventRenderData

@Adat
class OnSecondaryUp(
    override val handler: (event: UIEvent) -> Unit
) : UIEventHandler {
    override fun apply(subject: Any) {
        event(subject) {
            it.additionalEvents = true
            it.onSecondaryUp = this
        }
    }
}