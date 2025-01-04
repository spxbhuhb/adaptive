/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import `fun`.adaptive.ui.instruction.event.OnClick
import `fun`.adaptive.ui.instruction.event.UIEvent
import `fun`.adaptive.ui.render.applyText
import `fun`.adaptive.utility.firstOrNullIfInstance
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UILabel
import platform.UIKit.UIView

@AdaptiveActual(aui)
class AuiText(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<UIView>(adapter, parent, index, 1, 2) {

    override val receiver = AUILabel(this)

    init {
        receiver.tag = id
    }

    val content: String get() = state[0]?.toString() ?: ""

    @OptIn(ExperimentalForeignApi::class)
    override fun auiPatchInternal() {

        if (haveToPatch(dirtyMask, 1)) {

            // we have to apply instructions if they haven't changed
            // instructions sets the actual text fields of the UILabel
            // we don't have to do this if the instructions has been just applied
            // TODO think about layout effects of text changes

            if (renderData.text == null) {
                renderData.text = uiAdapter.defaultTextRenderData
            }

            val content = this.content
            val contentChange = (content != receiver.text)
            val styleChange = (renderData.text != previousRenderData.text)

            if (! haveToPatch(dirtyMask, 1) && ! contentChange && ! styleChange) {
                return
            }

            if (contentChange) {
                receiver.text = content
            }

            if (styleChange) {
                applyText(this)
            }

            receiver.intrinsicContentSize.useContents {
                renderData.innerWidth = this.width
                renderData.innerHeight = this.height
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    class AUILabel(
        val fragment: AuiText
    ) : UILabel(CGRectMake(0.0, 0.0, 0.0, 0.0)) {

        override fun touchesEnded(touches: Set<*>, withEvent: platform.UIKit.UIEvent?) {
            fragment.instructions.firstOrNullIfInstance<OnClick>()
                ?.execute(UIEvent(fragment, withEvent))
            super.touchesBegan(touches, withEvent)
        }

    }
}