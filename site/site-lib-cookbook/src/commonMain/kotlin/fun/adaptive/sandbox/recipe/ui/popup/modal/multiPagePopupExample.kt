package `fun`.adaptive.sandbox.recipe.ui.popup.modal

import `fun`.adaptive.cookbook.generated.resources.dining
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.example
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.popup.modal.MultiPageModalBackend
import `fun`.adaptive.ui.popup.modal.multiPageModal
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.support.UiSave


class MultiPageModalExampleBackend : UiClose, UiSave, MultiPageModalBackend<FragmentKey> {

    override val title: String
        get() = Strings.example

    override val pages = storeFor {
        listOf(
            MenuItem(Graphics.dining, "Page 1", "page1"),
            MenuItem(Graphics.dining, "Page 2", "page2"),
            MenuItem(Graphics.dining, "Page 3", "page3")
        )
    }

    override val selectedPage = storeFor<MenuItem<FragmentKey>?> { null }

    // this field is for demonstration only, replace them with your own data
    val messages = storeFor { emptyList<String>() }

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
fun multiPageModalExample(): AdaptiveFragment {

    multiPageModal(MultiPageModalExampleBackend()) { modalBackend ->
        size(600.dp, 400.dp)

        column {
            when (modalBackend.selected) {
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
    val modalBackend = fragment().firstContext<MultiPageModalExampleBackend>()

    column {
        maxSize .. verticalScroll
        text("This is the content of page 1")
        messages(modalBackend)
    }
}

@Adaptive
fun page2() {
    val modalBackend = fragment().firstContext<MultiPageModalExampleBackend>()

    column {
        maxSize .. verticalScroll
        text("This is the content of page 2")
        messages(modalBackend)
    }
}

@Adaptive
fun page3() {
    val modalBackend = fragment().firstContext<MultiPageModalExampleBackend>()

    column {
        maxSize .. verticalScroll
        text("This is the content of page 3")
        messages(modalBackend)
    }
}

@Adaptive
fun messages(
    modalBackend: MultiPageModalExampleBackend
) {
    val observed = observe { modalBackend.messages }

    text("Messages:")
    for (message in observed) text(message) .. paddingLeft { 16.dp }
}