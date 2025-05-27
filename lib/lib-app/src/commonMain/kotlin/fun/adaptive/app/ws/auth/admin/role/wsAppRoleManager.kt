package `fun`.adaptive.app.ws.auth.admin.role

import `fun`.adaptive.app.ws.shared.wsContentHeader
import `fun`.adaptive.auth.model.RoleSpec
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.lib_app.generated.resources.addRole
import `fun`.adaptive.lib_app.generated.resources.edit
import `fun`.adaptive.lib_app.generated.resources.roles
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.checkbox.checkbox
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.generated.resources.edit
import `fun`.adaptive.ui.generated.resources.filterPlaceholder
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.input.InputConfig.Companion.inputConfig
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.mpw.MultiPaneTheme
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.value.AvValue

@Adaptive
fun wsAppRoleManager(pane: PaneDef<RoleManagerViewBackend>): AdaptiveFragment {

    val filterBackend = valueFrom { adatFormBackend(RoleFilter()) }
    val filter = filterBackend.inputValue

    val items = valueFrom { pane.viewBackend.roles }

    column {
        MultiPaneTheme.DEFAULT.contentPaneContainer

        wsContentHeader(Strings.roles) {
            row {
                gap { 16.dp }
                textEditor { filter.text } .. width { 200.dp } .. inputConfig(label = "", placeholder = Strings.filterPlaceholder)

                row {
                    button(Strings.addRole)
                    primaryPopup { hide ->
                        popupAlign.absoluteCenter(modal = true, 150.dp)
                        roleEditor(hide = hide) { pane.viewBackend.save(it, true) }
                    }
                }
            }
        }

        localContext(pane.viewBackend) {
            items(items.filter { filter.matches(it) }, filter.isEmpty())
        }
    }

    return fragment()
}

@Adaptive
private fun items(
    items: List<AvValue<RoleSpec>>?,
    emptyFilter: Boolean
) {
    column {
        maxSize .. verticalScroll .. gap { 16.dp }
        when {
            items == null -> text("..betöltés...")
            items.isEmpty() -> text(if (emptyFilter) "nincsenek fiókok felvéve" else "nincs a szűrésnek megfelelő fiók")
            else -> for (item in items) {
                item(item)
            }
        }
    }
}

@Adaptive
private fun item(item: AvValue<RoleSpec>) {
    val hover = hover()
    val popupState = InputContext()

    val background =
        when {
            hover == true -> backgrounds.surfaceHover
            else -> backgrounds.surface
        }

    grid {
        maxWidth .. height { 36.dp } .. alignItems.startCenter .. borders.outline
        paddingLeft { 32.dp } .. paddingRight { 16.dp } .. gap { 16.dp } .. noSelect

        colTemplate(
            1.fr,    // name
            1.fr,    // context
            80.dp,   // group
            24.dp,   // edit
        )

        gap { 8.dp }
        cornerRadius { 8.dp }
        background

        text(item.name) .. maxWidth
        text(item.spec.context) .. maxWidth
        checkbox(item.spec.group) { }

        box {
            if (hover || popupState.value.isPopupOpen) {
                actionIcon(Graphics.edit, Strings.edit)
                primaryPopup(popupState) { hide ->
                    popupAlign.absoluteCenter(modal = true, 150.dp)
                    roleEditor(item, hide) {
                        fragment().firstContext<RoleManagerViewBackend>().save(it, false)
                    }
                }
            }
        }

    }
}

