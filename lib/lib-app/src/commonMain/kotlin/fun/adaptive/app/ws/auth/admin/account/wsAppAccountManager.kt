package `fun`.adaptive.app.ws.auth.admin.account

import `fun`.adaptive.app.ws.auth.account.AccountEditorData
import `fun`.adaptive.app.ws.shared.wsContentHeader
import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.lib_app.generated.resources.*
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.datetime.instant
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.generated.resources.close
import `fun`.adaptive.ui.generated.resources.edit
import `fun`.adaptive.ui.generated.resources.filterPlaceholder
import `fun`.adaptive.ui.generated.resources.more_vert
import `fun`.adaptive.ui.generated.resources.notSet
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.tableIconTheme
import `fun`.adaptive.ui.input.InputConfig.Companion.inputConfig
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.emptyInst
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.ui.workspace.WorkspaceTheme
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsAppAccountManager(pane: WsPane<AccountManagerViewBackend>): AdaptiveFragment {

    val filterBackend = valueFrom { adatFormBackend(AccountFilter()) }
    val filter = filterBackend.inputValue

    val items = valueFrom { pane.viewBackend.accounts }

    column {
        WorkspaceTheme.DEFAULT.contentPaneContainer

        wsContentHeader(Strings.accounts) {
            row {
                gap { 16.dp } .. alignItems.endCenter

                localContext(filterBackend) {
                    textEditor { filter.text } .. width { 200.dp } .. inputConfig(label = "", placeholder = Strings.filterPlaceholder)
                }

                row {
                    button(Strings.addAccount)
                    primaryPopup { hide ->
                        popupAlign.absoluteCenter(modal = true, 150.dp)
                        accountEditorAdmin(hide = hide) { pane.viewBackend.save(it) }
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
private fun items(items: List<BasicAccountSummary>?, emptyFilter: Boolean) {
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

@Adaptive
private fun item(item: BasicAccountSummary) {
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
            1.fr,    // login
            1.fr,    // name
            1.fr,    // e-mail
            80.dp,   // activated
            80.dp,   // locked
            140.dp,  // last login
            24.dp,   // edit,
            24.dp    // more actions
        )

        gap { 8.dp }
        cornerRadius { 8.dp }
        background

        text(item.login) .. maxWidth
        text(item.name) .. maxWidth
        text(item.email.ifEmpty { Strings.notSet }) .. maxWidth .. emptyInst(item.email)

        friendlyOrAngry(item.activated, Graphics.check_circle, Graphics.close)
        friendlyOrAngry(! item.locked, Graphics.lock_open, Graphics.lock)

        instant(item.lastLogin) .. maxWidth .. alignSelf.endCenter .. textSmall

        box {
            if (hover || popupState.value.isPopupOpen) {
                actionIcon(Graphics.edit, Strings.edit)
                primaryPopup(popupState) { hide ->
                    popupAlign.absoluteCenter(modal = true, 150.dp)
                    accountEditorAdmin(
                        item.toAccountEditorData(),
                        hide = hide
                    ) {
                        fragment().firstContext<AccountManagerViewBackend>().save(it)
                    }
                }
            }
        }

        actionIcon(Graphics.more_vert, theme = tableIconTheme)
    }
}

