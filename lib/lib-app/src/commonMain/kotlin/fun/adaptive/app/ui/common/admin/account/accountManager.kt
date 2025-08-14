package `fun`.adaptive.app.ui.common.admin.account

import `fun`.adaptive.app.ui.common.user.account.AccountEditorData
import `fun`.adaptive.ui.mpw.fragments.contentPaneHeader
import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.lib_app.generated.resources.*
import `fun`.adaptive.lib_app.generated.resources.accounts
import `fun`.adaptive.lib_app.generated.resources.login
import `fun`.adaptive.lib_app.generated.resources.name
import `fun`.adaptive.lib_app.generated.resources.activated
import `fun`.adaptive.lib_app.generated.resources.locked
import `fun`.adaptive.lib_app.generated.resources.lastLogin
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.datetime.instant
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.generated.resources.*
import `fun`.adaptive.ui.generated.resources.edit
import `fun`.adaptive.ui.icon.ActionIconRowBackend
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.tableIconTheme
import `fun`.adaptive.ui.input.InputConfig.Companion.inputConfig
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.menu.MenuSeparator
import `fun`.adaptive.ui.mpw.MultiPaneTheme
import `fun`.adaptive.ui.popup.modal.dialog
import `fun`.adaptive.ui.table.TableItem
import `fun`.adaptive.ui.table.TableViewBackendBuilder.Companion.tableBackend
import `fun`.adaptive.ui.table.table
import `fun`.adaptive.ui.table.tableFilterTextInput
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.emptyInst
import `fun`.adaptive.ui.theme.iconColors
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.value.AvValue
import kotlin.collections.filter
import kotlin.text.filter
import kotlin.text.matches

@Adaptive
fun accountManager() : AdaptiveFragment {

    val viewBackend = AccountManagerViewBackend(fragment())
    val tableBackend = tableDef(viewBackend)

    val accounts = observe { viewBackend.accounts }.also { accList ->
        tableBackend.allItems = accList.map { TableItem(it) }.toMutableList()
        tableBackend.updateAndNotify()
    }

    column {
        MultiPaneTheme.DEFAULT.contentPaneContainer

        contentPaneHeader(Strings.accounts) {
            row {
                gap { 16.dp } .. alignItems.endCenter

                row {
                    submitButton(Strings.addAccount) {
                        dialog(viewBackend.workspace, AccountEditorData() to viewBackend, ::accountEditorAdmin)
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
    viewBackend : AccountManagerViewBackend
) = tableBackend<BasicAccountSummary> {

    stringCell {
        label = Strings.login
        get = { it.login }
        minWidth = 120.dp
    }

    stringCell {
        label = Strings.name
        get = { it.name }
        minWidth = 120.dp
    }

    stringCell {
        label = Strings.email
        get = { it.email.ifEmpty { Strings.notSet } }
        minWidth = 120.dp
    }

    iconCell {
        label = Strings.activated
        headerInstructions = { theme.headerCellText + alignSelf.center }
        instructions = { alignSelf.center .. if (it.activated) iconColors.onSurfaceFriendly else iconColors.onSurfaceAngry }
        get = { if (it.activated) Graphics.check_circle else Graphics.close }
        minWidth = 40.dp
    }

    iconCell {
        label = Strings.locked
        headerInstructions = { theme.headerCellText + alignSelf.center }
        instructions = { alignSelf.center .. instructionsOf(if (it.locked) iconColors.onSurfaceAngry else iconColors.onSurfaceFriendly) }
        get = { if (it.locked) Graphics.lock else Graphics.lock_open }
        minWidth = 40.dp
    }

    timeAgoCell {
        label = Strings.lastLogin
        get = { it.lastLogin }
        minWidth = 140.dp
    }

    actionsCell {
        label = Strings.actions
        minWidth = 120.dp
        width = 120.dp
        get = { value -> ActionIconRowBackend(otherActions = actions(value)) { contextActionHandler(viewBackend, it) } }
    }

}

private fun actions(item : BasicAccountSummary) = listOf<MenuItemBase<BasicAccountSummary>>(
    MenuItem(Graphics.edit, Strings.edit, item)
)

private fun contextActionHandler(
    viewBackend : AccountManagerViewBackend,
    menuItem : MenuItem<BasicAccountSummary>
) {
    when (menuItem.label) {
        Strings.edit -> {
            dialog(
                viewBackend.workspace,
                menuItem.data.toAccountEditorData() to viewBackend,
                ::accountEditorAdmin
            )
        }
    }
}

fun BasicAccountSummary.toAccountEditorData() = AccountEditorData(
    principalId,
    accountId,
    login,
    name,
    email,
    activated,
    locked,
    roles = roles
)

