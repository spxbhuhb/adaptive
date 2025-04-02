package `fun`.adaptive.app.ws.auth.admin

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.*
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.app.ws.auth.account.AccountEditorData
import `fun`.adaptive.auth.api.AuthPrincipalApi
import `fun`.adaptive.auth.api.AuthRoleApi
import `fun`.adaptive.auth.model.AuthPrincipal
import `fun`.adaptive.auth.model.AuthRole
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.cancel
import `fun`.adaptive.ui.button.ButtonTheme
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.checkbox.checkbox
import `fun`.adaptive.ui.datetime.instant
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.label.inputLabel
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.popup.PopupTheme
import `fun`.adaptive.ui.popup.modalPopupTitle
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.wrap.wrapFromTop
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.uppercaseFirstChar

@Adaptive
fun accountEditorAdmin(
    account: AccountEditorData? = null,
    hide: () -> Unit,
    save: (AccountEditorData) -> Unit
) {
    val popupState = valueFrom { InputContext() }

    // This is to prevent Safari overwriting the e-mail field on password change.
    // With autofill, Safari just finds whatever field it likes and changes the value.
    // Nothing else matters, but the field being disabled. So, I disable the input fields
    // on the form when the password change popup is open. This strangely works.
    // I lost a few years of my life by being very-very angry while I figured this out.

    val safariHack = InputContext(disabled = popupState.value.popupOpen)

    var copy = copyOf { account ?: AccountEditorData() }

    @Independent
    val principal = fetch { getService<AuthPrincipalApi>(adapter().transport).getOrNull(account?.uuid?.cast() ?: UUID.nil()) }

    val knownRoles = fetch { getService<AuthRoleApi>(adapter().transport).all() } ?: emptyList()

    val theme = PopupTheme.default

    column {
        theme.modalContainer

        wrapFromTop(26.dp, { modalPopupTitle(Strings.addAccount, theme, hide) }) {

            column {

                row {
                    editFields(copy, safariHack)
                    //roles()
                }

                buttons(hide) { save(copy) }

            }
        }
    }
}

@Adaptive
fun editFields(copy: AccountEditorData, safariHack: InputContext) {
    column {
        width { 400.dp } .. padding { 16.dp } .. gap { 8.dp } .. maxWidth

        withLabel(Strings.accountName) { state ->
            textInput(copy.login, state) { }
        }

        withLabel(Strings.name) { state ->
            textInput(copy.name, safariHack) { copy.update(copy::name, it) }
        }

        withLabel(Strings.email.uppercaseFirstChar()) { state ->
            textInput(copy.email, safariHack) { copy.update(copy::email, it) }
        }

    }
}

@Adaptive
fun buttons(hide: () -> Unit, save: () -> Unit) {
    row {
        maxWidth .. alignItems.end .. borderTop(colors.lightOutline)
        paddingVertical { 12.dp } .. paddingRight { 16.dp }
        gap { 12.dp }

        button(Strings.cancel, theme = ButtonTheme.noFocus) .. onClick { hide() }
        button(Strings.save) .. onClick { save() }
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

@Adaptive
fun roles(knownRoles: List<AuthRole>, principalRoles: List<AuthRole>) {

    var selectedRoles = principalRoles

    column {
        paddingLeft { 32.dp } .. paddingRight { 16.dp } .. height { 120.dp } .. gap { 8.dp }

        inputLabel { "Szerepkörök" }

        flowBox {
            gap { 8.dp }

            for (role in knownRoles) {
                row {
                    alignItems.center .. gap { 8.dp }

                    onClick {
                        if (role in selectedRoles) {
                            selectedRoles - role
                        } else {
                            selectedRoles + role
                        }
                    }

                    box {
                        size(24.dp, 24.dp) .. alignItems.center
                        checkbox(role in selectedRoles) { }
                    }

                    text(role.name) .. noSelect
                }
            }
        }
    }
}