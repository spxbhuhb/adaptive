package `fun`.adaptive.app.ui.common.admin.role

import `fun`.adaptive.auth.model.RoleSpec
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.lib_app.generated.resources.*
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.edit
import `fun`.adaptive.ui.icon.ActionIconRowBackend
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.mpw.MultiPaneTheme
import `fun`.adaptive.ui.mpw.fragments.contentPaneHeader
import `fun`.adaptive.ui.popup.modal.dialog
import `fun`.adaptive.ui.table.TableItem
import `fun`.adaptive.ui.table.TableViewBackendBuilder.Companion.tableBackend
import `fun`.adaptive.ui.table.table
import `fun`.adaptive.ui.table.tableFilterTextInput
import `fun`.adaptive.value.AvValue

@Adaptive
fun roleManager(): AdaptiveFragment {

    val viewBackend = RoleManagerViewBackend(fragment())
    val tableBackend = tableDef(viewBackend)

    val roles = observe { viewBackend.roles }.also {
        tableBackend.setAllItems(it ?: emptyList())
    }

    column {
        MultiPaneTheme.DEFAULT.contentPaneContainer

        contentPaneHeader(Strings.roles) {
            row {
                gap { 16.dp } .. alignItems.endCenter

                row {
                    submitButton(Strings.addRole) {
                        // open editor for new role via dialog wrapper
                        dialog(viewBackend.workspace, (null to viewBackend), ::roleEditorAdmin)
                    }
                }
            }
        }

        column {
            fillStrategy.constrain .. maxSize .. gap { 16.dp }
            tableFilterTextInput(tableBackend)
            table(tableBackend)
        }
    }

    return fragment()
}

private fun tableDef(
    viewBackend: RoleManagerViewBackend
) = tableBackend<AvValue<RoleSpec>> {

    stringCell {
        label = Strings.name
        get = { it.nameLike }
        minWidth = 160.dp
    }

    stringCell {
        label = Strings.context
        get = { it.spec.context }
        minWidth = 160.dp
    }

    actionsCell {
        get = { value -> ActionIconRowBackend(otherActions = actions(value)) { contextActionHandler(viewBackend, it) } }
    }
}

private fun actions(item: AvValue<RoleSpec>) = listOf<MenuItemBase<AvValue<RoleSpec>>>(
    MenuItem(Graphics.edit, Strings.edit, item)
)

private fun contextActionHandler(
    viewBackend: RoleManagerViewBackend,
    menuItem: MenuItem<AvValue<RoleSpec>>
) {
    when (menuItem.label) {
        Strings.edit -> {
            dialog(
                viewBackend.workspace,
                (menuItem.data to viewBackend),
                ::roleEditorAdmin
            )
        }
    }
}

