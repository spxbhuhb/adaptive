package `fun`.adaptive.sandbox.recipe.ui.menu

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.cookbook.generated.resources.dining
import `fun`.adaptive.cookbook.generated.resources.lock
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.sandbox.recipe.demo.goodmorning.textMedium
import `fun`.adaptive.sandbox.support.configureForm
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.form.AdatFormViewBackend
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.menu.MenuSeparator
import `fun`.adaptive.ui.menu.contextMenu

@Adaptive
fun contextMenuPlayground(): AdaptiveFragment {

    val form = valueFrom { adatFormBackend(ContextMenuPlaygroundConfig()) }

    flowBox {
        gap { 16.dp }
        contextMenuPlaygroundForm(form)
        contextMenuPlaygroundResult(form.inputValue)
    }

    return fragment()
}

@Adat
class ContextMenuPlaygroundConfig(
    val useSeparator: Boolean = true,
    val useLongItem: Boolean = true
)

@Adaptive
fun contextMenuPlaygroundForm(
    form: AdatFormViewBackend<ContextMenuPlaygroundConfig>
) {

    val template = ContextMenuPlaygroundConfig()

    configureForm(form) {
        column {
            width { 288.dp } .. gap { 16.dp } .. padding { 16.dp }

            column {
                booleanEditor { template.useSeparator }
                booleanEditor { template.useLongItem }
            }
        }
    }
}

@Adaptive
fun contextMenuPlaygroundResult(config: ContextMenuPlaygroundConfig) {

    var message = "Click on the button to open the context menu."

    val items = buildItems(config)

    column {
        column {
            button("Click here!")
            primaryPopup { hide ->
                contextMenu(items) { item, modifiers ->
                    message = "Clicked on ${item.label} with modifiers $modifiers"
                    hide()
                }
            }
        }
        text(message) .. textMedium
    }

}

fun buildItems(config: ContextMenuPlaygroundConfig): List<MenuItemBase<Any>> {
    val items = mutableListOf<MenuItemBase<Any>>()

    items += MenuItem(
        Graphics.lock,
        "Menu Item 1",
        "data-1"
    )

    if (config.useSeparator) items += MenuSeparator()

    items += MenuItem(
        Graphics.menu_book,
        "Menu Item 2",
        "data-2"
    )

    if (config.useLongItem) {
        items += MenuItem(
            Graphics.dining,
            "Menu Item with a long-long name, hopefully more than 200.dp",
            "data-2"
        )
    }

    return items
}
