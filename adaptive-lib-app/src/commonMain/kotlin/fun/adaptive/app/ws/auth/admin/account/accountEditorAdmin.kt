package `fun`.adaptive.app.ws.auth.admin.account

import `fun`.adaptive.adaptive_lib_app.generated.resources.*
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.app.ws.auth.account.AccountEditorData
import `fun`.adaptive.auth.api.AuthRoleApi
import `fun`.adaptive.auth.model.AuthPrincipal
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.checkbox.checkbox
import `fun`.adaptive.ui.datetime.instant
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.label.inputLabel
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.popup.modalEditor
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.utility.uppercaseFirstChar
import `fun`.adaptive.value.AvValueId

@Adaptive
fun accountEditorAdmin(
    account: AccountEditorData? = null,
    hide: () -> Unit,
    save: (AccountEditorData) -> Unit
) {
    @Independent
    var copy = copyOf { account ?: AccountEditorData() }
    val title = if (account == null) Strings.addAccount else Strings.editAccount

    modalEditor(title, hide, { save(copy); hide() }) {
        row {
            editFields(copy)
            roles(copy)
        }
    }
}

@Adaptive
fun editFields(copy: AccountEditorData) {

    val accountNameState = InputContext(invalid = copy.principalName.isEmpty())

    val passwordState = InputContext(
        invalid = copy.password != copy.passwordConfirm
    )

    column {
        width { 400.dp } .. padding { 16.dp } .. gap { 8.dp } .. borderRight(colors.lightOutline)

        withLabel(Strings.accountName, accountNameState) { state ->
            textInput(copy.principalName, state) { copy.update(copy::principalName, it) }
        }

        withLabel(Strings.name) { state ->
            textInput(copy.name) { copy.update(copy::name, it) }
        }

        withLabel(Strings.email.uppercaseFirstChar()) { state ->
            textInput(copy.email) { copy.update(copy::email, it) }
        }

        withLabel(Strings.password.uppercaseFirstChar(), passwordState) { s ->
            textInput(copy.password, s) { v -> copy.update(copy::password, v) } .. secret
        }

        withLabel(Strings.confirmPassword, passwordState) { s ->
            textInput(copy.passwordConfirm, s) { v -> copy.update(copy::passwordConfirm, v); } .. secret
        }
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
fun roles(editorData: AccountEditorData) {

    val knownRoles = fetch { getService<AuthRoleApi>(adapter().transport).all() } ?: emptyList()

    var selectedRoles = editorData.roles

    column {
        padding(16.dp) .. gap { 8.dp }
        width { 320.dp } .. height { 300.dp } .. verticalScroll

        withLabel(Strings.roles) {
            for (role in knownRoles) {
                row {
                    alignItems.startCenter .. gap { 8.dp } .. paddingTop { 8.dp }

                    onClick {
                        if (role.uuid in selectedRoles) {
                            editorData.roles(selectedRoles - role.uuid)
                        } else {
                            editorData.roles(selectedRoles + role.uuid)
                        }
                    }

                    box {
                        size(24.dp, 24.dp) .. alignItems.center
                        checkbox(role.uuid in selectedRoles) { v ->
                            if (v) {
                                editorData.roles(selectedRoles - role.uuid)
                            } else {
                                editorData.roles(selectedRoles + role.uuid)
                            }
                        }
                    }

                    text(role.name) .. noSelect
                }
            }
        }
    }
}