package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.*
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.auth.api.AuthPrincipalApi
import `fun`.adaptive.auth.api.AuthRoleApi
import `fun`.adaptive.auth.model.AuthPrincipal
import `fun`.adaptive.auth.model.AuthRole
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.account
import `fun`.adaptive.ui.builtin.check
import `fun`.adaptive.ui.button.ButtonTheme
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.button.dangerButton
import `fun`.adaptive.ui.checkbox.checkbox
import `fun`.adaptive.ui.datetime.instant
import `fun`.adaptive.ui.dialog.dangerButtonDialog
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.label.inputLabel
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.uppercaseFirstChar
import kotlinx.coroutines.launch

@Adaptive
fun accountEditor(
    account: AccountEditorData? = null,
    save : (AccountEditorData) -> Unit
) {

    var copy = copyOf { account ?: AccountEditorData() }

    @Independent
    val principal = fetch { getService<AuthPrincipalApi>(adapter().transport).getOrNull(account?.uuid?.cast() ?: UUID.nil()) }

    val knownRoles = fetch { getService<AuthRoleApi>(adapter().transport).all() } ?: emptyList()
    //val principalRoles = fetch { getService<AuthRoleApi>(adapter().transport).rolesOf(account?.id?.cast() ?: UUID.nil(), null) } ?: emptyList()


    column {
        padding { 16.dp }
        gap { 16.dp }

        row {
            maxWidth .. spaceBetween

            column {
                h2(Strings.account)
                uuidLabel { copy.uuid }
            }

            row {
                gap { 16.dp }
                button(Strings.passwordChange, theme = ButtonTheme.noFocus) .. onClick { }
                button(Strings.save) .. onClick { save(copy) }
            }
        }

        withLabel(Strings.accountName, InputContext(disabled = true)) { state ->
            width { 400.dp }
            textInput(copy.login, state) { }
        }

        withLabel(Strings.name) { state ->
            width { 400.dp }
            textInput(copy.name) { copy.update(copy::name, it) }
        }

        withLabel(Strings.email.uppercaseFirstChar()) { state ->
            width { 400.dp }
            textInput(copy.email) { copy.update(copy::email, it) }
        }
    }

//    grid {
//        rowTemplate(72.dp.repeat(rowCount), 86.dp)
//        width { 708.dp } .. height { (24 + 72 * rowCount + rowCount * 16 + 120 + 86).dp }
//        paddingTop { 24.dp } .. gap { 16.dp }
//
//        line { common(copy) }
//        line { name(copy) }
//        line { email(copy) }
//
//        if (principal != null) loginTimes(principal)
//        if (principal != null) loginCounters(principal)
//
//        //roles(knownRoles, principalRoles)
//
//        buttons(account, copy, close)
//    }

}

@Adaptive
fun line(@Adaptive content: () -> Unit) {
    box {
        maxWidth .. height { 72.dp }
        paddingLeft { 32.dp } .. paddingRight { 16.dp }
        content()
    }
}

@Adaptive
fun common(account: AccountEditorData) {
    grid {
        maxWidth
        rowTemplate(28.dp, 44.dp)
        colTemplate(1.fr, 100.dp, 100.dp)
        alignItems.center

        inputLabel { "Fiók név" } .. alignSelf.startCenter
        inputLabel { "Aktivált" }
        inputLabel { "Zárolt" }

        editor { account.login } .. width { 300.dp } .. alignSelf.startCenter
        editor { account.activated }
        editor { account.locked }
    }
}

@Adaptive
fun name(account: AccountEditorData) {
    grid {
        maxWidth
        rowTemplate(28.dp, 44.dp)
        alignItems.startCenter

        inputLabel { "Név" }
        editor { account.name } .. width { 600.dp }
    }
}

@Adaptive
fun email(account: AccountEditorData) {
    grid {
        rowTemplate(28.dp, 44.dp)
        alignItems.startCenter
        gapWidth { 32.dp }

        inputLabel { "E-mail cím" }
        editor { account.email } .. width { 600.dp }
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

@Adaptive
fun buttons(account: AccountEditorData?, copy: AccountEditorData, close: () -> Unit) {
    row {
        height { 86.dp } .. maxWidth
        alignItems.center .. spaceBetween .. marginTop { 16.dp } .. paddingHorizontal { 32.dp }
        borderTop(colors.outline)

        row {
            gap { 16.dp }

            dangerButtonDialog("Reset", Graphics.gpp_maybe, "Network Reset") { closeInner ->
//                resetNetwork(copy) {
//                    closeInner()
//                }
            }

            dangerButton("Csatlakoztatás", Graphics.gpp_maybe) .. onClick {
//                io {
//                    modelService.addCommand(EnableJoin(copy.id))
//                    info("Csatlakozás engedélyezve.")
//                    close()
//                }
            }
        }

        button("Mentés", Graphics.check) .. onClick {
            adapter().scope.launch {
                if (account == null) {
                    // add account
                } else {
                    // update account
                }
            }
            close()
        }
    }
}