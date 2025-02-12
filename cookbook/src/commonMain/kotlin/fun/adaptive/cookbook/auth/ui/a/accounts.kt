package `fun`.adaptive.cookbook.auth.ui.a

import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.cookbook.add
import `fun`.adaptive.cookbook.auth.api.AccountApi
import `fun`.adaptive.cookbook.auth.model.AccountSummary
import `fun`.adaptive.cookbook.auth.ui.account.AccountEditorData
import `fun`.adaptive.cookbook.auth.ui.account.AccountFilter
import `fun`.adaptive.cookbook.auth.ui.account.accountEditor
import `fun`.adaptive.cookbook.check_circle
import `fun`.adaptive.cookbook.close
import `fun`.adaptive.cookbook.edit
import `fun`.adaptive.cookbook.lock
import `fun`.adaptive.cookbook.lock_open
import `fun`.adaptive.cookbook.shared.cornerRadius8
import `fun`.adaptive.cookbook.shared.title
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.api.inputPlaceholder
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.datetime.datetime
import `fun`.adaptive.ui.dialog.api.buttonDialog
import `fun`.adaptive.ui.dialog.api.iconDialog
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.emptyInst
import `fun`.adaptive.ui.theme.iconColors
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
fun header(filter: AccountFilter) {
    grid {
        colTemplate(400.dp, 1.fr, 200.dp) .. height { 118.dp } .. maxWidth

        title("Fiókok") .. alignSelf.startCenter

        editor { filter.text } .. alignSelf.center .. width { 400.dp } .. inputPlaceholder { "fiókok szűrése" }

        buttonDialog("Fiók", Graphics.add, "Fiók hozzáadása") { accountEditor(close = it) } .. alignSelf.endCenter
    }
}

@Adaptive
fun items(items: List<AccountSummary>?, emptyFilter: Boolean) {
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