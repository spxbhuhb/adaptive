package `fun`.adaptive.app.ws.auth.admin.account

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.app.ws.auth.account.AccountEditorData
import `fun`.adaptive.auth.api.AuthRoleApi
import `fun`.adaptive.auth.model.AuthPrincipal
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.lib_app.generated.resources.addAccount
import `fun`.adaptive.lib_app.generated.resources.editAccount
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.datetime.instant
import `fun`.adaptive.ui.editor.multiSelectMappingEditor
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.input.select.item.selectInputOptionCheckbox
import `fun`.adaptive.ui.input.select.mapping.SelectOptionMapping
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.label.inputLabel
import `fun`.adaptive.ui.popup.modalForEdit
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem

@Adaptive
fun accountEditorAdmin(
    account: AccountEditorData? = null,
    hide: () -> Unit,
    save: (AccountEditorData) -> Unit
) {
    val form = adatFormBackend(account ?: AccountEditorData()) {
        expectEquals(it::password, it::confirmPassword, dualTouch = true)
    }

    val title = if (account == null) Strings.addAccount else Strings.editAccount

    modalForEdit(title, hide, { save(form.value); hide() }) {
        row {
            localContext(form) {
                editFields()
                roles()
            }
        }
    }
}

@Adaptive
fun editFields() {

    val template = AccountEditorData()

    column {
        width { 400.dp } .. padding { 16.dp } .. gap { 8.dp } .. borderRight(colors.lightOutline)

        textEditor { template.principalName }
        textEditor { template.name }
        textEditor { template.email }
        textEditor { template.password }
        textEditor { template.confirmPassword }
    }
}

@Adaptive
fun loginTimes(principal: AuthPrincipal) {
    grid {
        maxWidth
        rowTemplate(28.dp, 44.dp)
        colTemplate(1.fr.repeat(2))
        gapWidth { 32.dp }
        alignItems.startCenter

        inputLabel { "Utolsó sikeres azonosítás" }
        inputLabel { "Utolsó sikertelen azonosítás" }

        instant(principal.spec.lastAuthSuccess)
        instant(principal.spec.lastAuthFail)
    }
}


@Adaptive
fun loginCounters(principal: AuthPrincipal) {
    grid {
        maxWidth
        rowTemplate(28.dp, 44.dp)
        colTemplate(1.fr.repeat(2))
        gapWidth { 32.dp }
        alignItems.startCenter

        inputLabel { "Sikeres azonosítások száma" }
        inputLabel { "Sikertelen azonosítások száma" }

        text(principal.spec.authSuccessCount)
        text(principal.spec.authFailCount)
    }
}

fun AccountEditorData.roles(roles: Set<AvValueId>) {
    update(this::roles, roles)
}

@Adaptive
fun roles() {

    val template = AccountEditorData()
    val knownRoles = fetch { getService<AuthRoleApi>(adapter().transport).all() } ?: emptyList()

    column {
        padding { 16.dp } .. width { 300.dp } .. height { 300.dp }
        multiSelectMappingEditor(knownRoles, AvItemSelectMapping(), { selectInputOptionCheckbox(it) }) { template.roles } .. maxSize
    }
}

class AvItemSelectMapping : SelectOptionMapping<AvValueId, AvItem<*>> {
    override fun optionToValue(option: AvItem<*>): AvValueId = option.uuid
}