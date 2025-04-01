package `fun`.adaptive.app.ws.auth.admin

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.*
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.app.ws.auth.account.AccountEditorData
import `fun`.adaptive.app.ws.shared.wsContentHeader
import `fun`.adaptive.auth.api.basic.AuthBasicApi
import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.close
import `fun`.adaptive.ui.builtin.more_vert
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.datetime.instant
import `fun`.adaptive.ui.dialog.rowIconDialog
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.tableIconTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.emptyInst
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.ui.workspace.WorkspaceTheme
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsAppAccountManager(pane: WsPane<*, *>): AdaptiveFragment {

    accountList()

    return fragment()
}


@Adaptive
fun accountList() {
    val filter = copyOf { AccountFilter() }
    val items = fetch { getService<AuthBasicApi>(adapter().transport).accounts() }

    column {
        WorkspaceTheme.DEFAULT.contentPaneContainer

        wsContentHeader(Strings.accounts) {
            row {
                gap { 16.dp }
                editor { filter.text } .. width { 200.dp } .. inputPlaceholder { Strings.filter }

                row {
                    button(Strings.addAccount)
                    primaryPopup { hide ->
                        popupAlign.absoluteCenter(modal = true, 150.dp)
                        accountEditorAdmin(hide = hide) { }
                    }
                }
            }
        }

        items(items?.filter { filter.matches(it) }, filter.isEmpty())
    }
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

@Adaptive
private fun item(item: BasicAccountSummary) {
    val hover = hover()
    var modalOpen = false

    val background =
        when {
            hover == true -> backgrounds.surfaceHover
            else -> backgrounds.surface
        }

    grid {
        maxWidth .. height { 36.dp } .. alignItems.startCenter .. borders.outline
        paddingLeft { 32.dp } .. paddingRight { 16.dp } .. gap { 16.dp }

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
        text(item.email.ifEmpty { "(nincs email)" }) .. maxWidth .. emptyInst(item.email)

        friendlyOrAngry(item.activated, Graphics.check_circle, Graphics.close)
        friendlyOrAngry(! item.locked, Graphics.lock_open, Graphics.lock)

        instant(item.lastLogin) .. maxWidth .. alignSelf.endCenter .. textSmall

        box {
            if (hover || modalOpen) {
                rowIconDialog(Graphics.edit, "Fiók szerkesztése", feedback = { modalOpen = it }) {
                    accountEditorAdmin(
                        AccountEditorData(
                            item.accountId,
                            item.login,
                            item.name,
                            item.email,
                            item.activated,
                            item.locked
                        ),
                        hide = { }
                    ) { }
                }
            }
        }

        actionIcon(Graphics.more_vert, theme = tableIconTheme)
    }
}

