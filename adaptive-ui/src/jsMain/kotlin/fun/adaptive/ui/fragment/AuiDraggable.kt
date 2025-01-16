/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.adat.encodeToJson
import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import `fun`.adaptive.ui.fragment.layout.AbstractBox
import `fun`.adaptive.ui.instruction.event.TransferData
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@AdaptiveActual(aui)
open class AuiDraggable(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractBox<HTMLElement, HTMLDivElement>(adapter, parent, declarationIndex) {

    init {
        receiver.draggable = true

        receiver.ondragstart = { event ->
            val transferData = instructions.firstOrNullIfInstance<TransferData>()
            // TODO missing transferData should result in syntax error before compilation
            checkNotNull(transferData) { "draggable fragment without `transferData` annotation" }

            event.dataTransfer?.apply {
                setData("application/json", transferData.encodeToJson())
            }

        }
    }

}