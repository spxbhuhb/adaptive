/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.event

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.render.event

@Adat
class OnClick(
    override val handler: (event: UIEvent) -> Unit,
    val feedbackText: String? = null,
    val feedbackIcon: GraphicsResourceSet? = null
) : UIEventHandler {
    override fun applyTo(subject: Any) {
        event(subject) { it.onClick = this }
    }
}