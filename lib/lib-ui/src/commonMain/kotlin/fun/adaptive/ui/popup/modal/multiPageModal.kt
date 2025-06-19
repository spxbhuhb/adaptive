package `fun`.adaptive.ui.popup.modal

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.nop
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.general.Observable
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.generated.resources.example
import `fun`.adaptive.ui.generated.resources.pleaseSelectFromList
import `fun`.adaptive.ui.input.select.item.selectInputOptionIconAndText
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.input.select.selectInputList
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.splitpane.verticalSplitDivider
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textMedium


interface MultiPageModalBackend<MENU_ITEM_DATA> {

    /**
     * List of the pages that shows on the left side of the modal.
     */
    val pages: Observable<List<MenuItem<MENU_ITEM_DATA>>>

    val selectedPage: Observable<MenuItem<MENU_ITEM_DATA>?>

    fun selectPage(page: MenuItem<MENU_ITEM_DATA>?)

    val noPageMessage: String
        get() = Strings.pleaseSelectFromList

}

@Adaptive
fun <MENU_ITEM_DATA> multiPageModal(
    viewBackend: MultiPageModalBackend<MENU_ITEM_DATA>? = null,
    @Adaptive _fixme_adaptive_content: (MultiPageModalBackend<MENU_ITEM_DATA>) -> Unit
): AdaptiveFragment {

    val modalBackend = viewBackend ?: fragment().firstContext()
    val pages = valueFrom { modalBackend.pages }
    val selectedPage = valueFrom { modalBackend.selectedPage }

    val selectListBackend = selectInputBackend(
        selectedPage
    ) {
        options = pages
        toText = { it.label }
        toIcon = { it.icon ?: Graphics.empty }
        onChange = { if (selectedPage != it) modalBackend.selectPage(it) }
    }

    // FIXME split store mess
    val splitConfigStore = storeFor { SplitPaneViewBackend(SplitVisibility.Both, SplitMethod.FixFirst, 200.0, Orientation.Horizontal) }
    val splitConfig = valueFrom { splitConfigStore }

    localContext(viewBackend) {
        basicModal(Strings.example) {
            instructions()
            splitPane(
                splitConfig,
                {
                    column {
                        maxSize .. verticalScroll .. padding { 8.dp } .. backgrounds.surface
                        selectInputList(selectListBackend, { selectInputOptionIconAndText(it) })
                    }
                },
                { verticalSplitDivider() },
                {
                    box {
                        maxSize .. padding { 16.dp } .. (if (selectedPage == null) nop else verticalScroll)

                        if (selectedPage == null) {
                            text(modalBackend.noPageMessage) .. alignSelf.center .. textColors.onSurfaceMedium .. textMedium
                        } else {
                            localContext(viewBackend) {
                                _fixme_adaptive_content(modalBackend)
                            }
                        }
                    }
                }
            ) .. borderTop(colors.lightOutline) .. maxSize
        }
    }

    return fragment()
}