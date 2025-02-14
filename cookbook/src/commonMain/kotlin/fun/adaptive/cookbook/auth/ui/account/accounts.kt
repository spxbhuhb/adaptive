package `fun`.adaptive.cookbook.auth.ui.account

import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.cookbook.*
import `fun`.adaptive.cookbook.auth.api.AccountApi
import `fun`.adaptive.cookbook.auth.model.AccountSummary
import `fun`.adaptive.cookbook.shared.cornerRadius8
import `fun`.adaptive.cookbook.shared.title
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.more_vert
import `fun`.adaptive.ui.datetime.instant
import `fun`.adaptive.ui.dialog.api.buttonDialog
import `fun`.adaptive.ui.dialog.api.rowIconDialog
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.tableIconTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.emptyInst
import `fun`.adaptive.ui.theme.textSmall

@Adaptive
fun accounts() {
    val filter = copyStore { AccountFilter() }
    val items = fetch { getService<AccountApi>(adapter().transport).accounts() }

    grid {
        maxSize .. colTemplate(1.fr) .. rowTemplate(118.dp, 1.fr) .. paddingHorizontal { 16.dp }

        header(filter)
        items(items?.filter { filter.matches(it) }, filter.isEmpty())
    }
}

@Adaptive
private fun header(filter: AccountFilter) {
    grid {
        colTemplate(400.dp, 1.fr, 200.dp) .. height { 118.dp } .. maxWidth

        title("Fiókok") .. alignSelf.startCenter

        editor { filter.text } .. alignSelf.center .. width { 400.dp } .. inputPlaceholder { "fiókok szűrése" }

        buttonDialog("Fiók", Graphics.add, "Fiók hozzáadása") { accountEditor(close = it) } .. alignSelf.endCenter
    }
}

@Adaptive
private fun items(items: List<AccountSummary>?, emptyFilter: Boolean) {
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
private fun item(item: AccountSummary) {
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
            1.fr,    // phone
            80.dp,   // activated
            80.dp,   // locked
            140.dp,  // last login
            24.dp,   // edit,
            24.dp    // more actions
        )

        gap { 8.dp }
        cornerRadius8
        background

        text(item.login) .. maxWidth
        text(item.name) .. maxWidth
        text(item.email.ifEmpty { "(nincs email)" }) .. maxWidth .. emptyInst(item.email)
        text(item.phone.ifEmpty { "(nincs telefonszám)" }) .. maxWidth .. emptyInst(item.phone)

        friendlyOrAngry(item.activated, Graphics.check_circle, Graphics.close)
        friendlyOrAngry(! item.locked, Graphics.lock_open, Graphics.lock)

        instant(item.lastLogin) .. maxWidth .. alignSelf.endCenter .. textSmall

        box {
            if (hover || modalOpen) {
                rowIconDialog(Graphics.edit, "Fiók szerkesztése", feedback = { modalOpen = it }) {
                    accountEditor(
                        AccountEditorData(
                            item.id,
                            item.login,
                            item.name,
                            item.email,
                            item.phone,
                            item.activated,
                            item.locked
                        ),
                        it
                    )
                }
            }
        }

        actionIcon(Graphics.more_vert, theme = tableIconTheme)
    }
}