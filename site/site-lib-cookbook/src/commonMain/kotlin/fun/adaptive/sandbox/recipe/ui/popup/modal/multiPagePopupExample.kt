package `fun`.adaptive.sandbox.recipe.ui.popup.modal

import `fun`.adaptive.cookbook.generated.resources.dining
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.adaptiveStoreFor
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.popup.modal.MultiPageModalBackend
import `fun`.adaptive.ui.popup.modal.multiPageModal
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.support.UiSave
import `fun`.adaptive.utility.debug


class MultiPagePopupExampleBackend : UiClose, UiSave, MultiPageModalBackend<FragmentKey> {

    override val pages = storeFor {
        listOf(
            MenuItem(Graphics.dining, "Page 1", "page1"),
            MenuItem(Graphics.dining, "Page 2", "page2"),
            MenuItem(Graphics.dining, "Page 3", "page3")
        )
    }

    override val selectedPage = storeFor<MenuItem<FragmentKey>?> { null }

    // this field is for demonstration only, replace them with your own data
    val messages = adaptiveStoreFor(emptyList<String>())

    override fun selectPage(page: MenuItem<FragmentKey>?) {
        selectedPage.value = page
        messages.value = emptyList()
    }

    override fun uiClose() {
        messages.value += "close"
    }

    override fun uiSave(close: UiClose) {
        messages.value += "save"
    }

}

@Adaptive
fun multiPagePopupExample(): AdaptiveFragment {

    multiPageModal(MultiPagePopupExampleBackend()) { modalBackend ->
        size(600.dp, 400.dp)

        column {
            when (modalBackend.selectedPage.value?.data?.debug()) {
                "page1" -> page1()
                "page2" -> page2()
                "page3" -> page3()
            }
        }
    }

    return fragment()
}

@Adaptive
fun page1() {
    val modalBackend = fragment().firstContext<MultiPagePopupExampleBackend>()

    column {
        maxSize .. verticalScroll
        text("This is the content of page 1")
        messages(modalBackend)
    }
}

@Adaptive
fun page2() {
    val modalBackend = fragment().firstContext<MultiPagePopupExampleBackend>()

    column {
        maxSize .. verticalScroll
        text("This is the content of page 2")
        messages(modalBackend)
    }
}

@Adaptive
fun page3() {
    val modalBackend = fragment().firstContext<MultiPagePopupExampleBackend>()

    column {
        maxSize .. verticalScroll
        text("This is the content of page 3")
        messages(modalBackend)
    }
}

@Adaptive
fun messages(
    modalBackend: MultiPagePopupExampleBackend
) {
    val observed = valueFrom { modalBackend.messages }

    text("Messages:")
    for (message in observed) text(message) .. paddingLeft { 16.dp }
}