package `fun`.adaptive.ui.fragment

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.render.model.AuiRenderData

abstract class AbstractText<RT>(
    adapter: AbstractAuiAdapter<RT, *>,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<RT>(adapter, parent, index, 2) {

    private val content: Any?
        get() = get(1)

    override fun auiPatchInternal() {
        val safeContent = content?.toString() ?: ""
        val contentChange = (isInit || safeContent != getReceiverContent())
        val styleChange = (renderData.text != previousRenderData.text)

        if (! haveToPatch(dirtyMask, 1) && ! contentChange && ! styleChange) {
            return
        }

        if (haveToPatch(dirtyMask, 1) || styleChange) {
            alignText()
        }

        if (contentChange) {
            setReceiverContent(safeContent)

            // no instruction change, however, the content changed so we have to re-layout
            if (renderData === previousRenderData) {
                renderData = AuiRenderData(uiAdapter, previousRenderData, uiAdapter.themeFor(this), instructions)
                if (renderData.text == null) {
                    renderData.text = uiAdapter.defaultTextRenderData
                }
                scheduleUpdate()
            }
        }

        measureText(safeContent)
    }

    abstract fun measureText(content: String)

    abstract fun setReceiverContent(content: String)

    abstract fun getReceiverContent(): String?

    open fun alignText() {

    }
}
