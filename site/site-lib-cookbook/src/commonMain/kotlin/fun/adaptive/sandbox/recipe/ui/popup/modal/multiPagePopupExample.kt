package `fun`.adaptive.sandbox.recipe.ui.popup.modal

import `fun`.adaptive.cookbook.generated.resources.dining
import `fun`.adaptive.cookbook.generated.resources.flatware
import `fun`.adaptive.cookbook.generated.resources.pleaseSelectFromList
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.resolve.resolveString
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend
import `fun`.adaptive.ui.generated.resources.example
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.input.select.item.selectInputOptionIconAndText
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.input.select.selectInputList
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.popup.modalPopup
import `fun`.adaptive.ui.splitpane.verticalSplitDivider
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.support.UiSave
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textMedium
import `fun`.adaptive.ui.value.iconCache


class ExampleDialogBackend : UiClose, UiSave {

    val selectedPage = storeFor<String?> { null }

    val pages = listOf("Page 1", "Page 2", "Page 3")

    fun onSavePopup(hide: () -> Unit) {

    }

    override fun uiClose() {

    }

    override fun uiSave(close: UiClose) {

    }
}

@Adaptive
fun multiPagePopupExample() : AdaptiveFragment {
    val viewBackend = ExampleDialogBackend()

    // FIXME split store mess
    val splitConfigStore = storeFor { SplitPaneViewBackend(SplitVisibility.Both, SplitMethod.FixFirst, 200.0, Orientation.Horizontal) }
    val splitConfig = valueFrom { splitConfigStore }

    localContext(viewBackend) {
        modalPopup(Strings.example) {
            size(600.dp, 400.dp)
            splitPane(
                splitConfig,
                { pageList(viewBackend) },
                { verticalSplitDivider() },
                { pageContent(viewBackend) }
            ) .. borderTop(colors.lightOutline) .. maxSize
        }
    }

    return fragment()
}

@Adaptive
private fun pageList(
    viewBackend: ExampleDialogBackend
) {
    iconCache["Page 1"] = Graphics.dining
    iconCache["Page 2"] = Graphics.menu_book
    iconCache["Page 3"] = Graphics.flatware

    val backend = selectInputBackend(
        viewBackend.selectedPage.value
    ) {
        options = viewBackend.pages
        toText = { fragment().resolveString(it) }
        toIcon = { iconCache[it] ?: Graphics.dining }
        onChange = { viewBackend.selectedPage.value = it }
    }

    column {
        maxSize .. verticalScroll .. padding { 8.dp } .. backgrounds.surface
        selectInputList(backend, { selectInputOptionIconAndText(it) })
    }
}

@Adaptive
private fun pageContent(
    viewBackend: ExampleDialogBackend
) {
    val selectedPage = valueFrom { viewBackend.selectedPage }

    column {
        maxSize .. verticalScroll .. padding { 16.dp }

        if (selectedPage == null) {
            text(Strings.pleaseSelectFromList) .. alignSelf.center .. textColors.onSurfaceMedium .. textMedium
        } else {
            localContext(viewBackend) {
                text(selectedPage)
            }
        }
    }
}