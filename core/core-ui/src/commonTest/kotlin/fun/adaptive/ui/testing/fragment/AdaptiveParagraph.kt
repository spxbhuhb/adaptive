/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.testing.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.aui
import `fun`.adaptive.ui.fragment.paragraph.AbstractParagraph
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem
import `fun`.adaptive.ui.testing.AuiTestAdapter
import `fun`.adaptive.ui.testing.TestReceiver

@AdaptiveActual(aui)
class AdaptiveParagraph(
    adapter: AuiTestAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractParagraph<TestReceiver, TestReceiver>(adapter, parent, declarationIndex) {

    override val receiver = TestReceiver()

    override fun auiPatchInternal() {

    }

    override fun placeRows(rows: List<Row>) {

    }

    override fun measureText(item: ParagraphItem, text: String, instructionSetIndex: Int) {

    }

}