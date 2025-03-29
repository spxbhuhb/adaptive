package `fun`.adaptive.cookbook.auth.ui.a

import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.cookbook.add
import `fun`.adaptive.auth.api.BasicAccountApi
import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.cookbook.auth.ui.account.AccountFilter
import `fun`.adaptive.cookbook.auth.ui.account.accountEditor
import `fun`.adaptive.cookbook.support.title
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.inputPlaceholder
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr

@Adaptive
fun accounts() {
    val filter = copyOf { AccountFilter() }
    val items = fetch { getService<BasicAccountApi>(adapter().transport).accounts() }

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
fun items(items: List<BasicAccountSummary>?, emptyFilter: Boolean) {
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