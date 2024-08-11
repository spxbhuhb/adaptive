/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.platform

import `fun`.adaptive.ui.common.render.BrowserEventApplier
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener

class BrowserEventListener(
    val eventName: String,
    val condition: BrowserEventApplier.EventCondition,
    val eventFun: BrowserEventListener.(Event) -> Unit
) : EventListener {
    override fun handleEvent(event: Event) {
        eventFun(event)
    }
}