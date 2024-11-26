package `fun`.adaptive.cookbook.auth.ui.account

import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.auth.api.PrincipalApi
import `fun`.adaptive.auth.api.RoleApi
import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.auth.model.Role
import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.check
import `fun`.adaptive.cookbook.gpp_maybe
import `fun`.adaptive.cookbook.shared.yellow
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.borderTop
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.gapWidth
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.marginTop
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.paddingTop
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.spaceBetween
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.builtin.check
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.button.api.dangerButton
import `fun`.adaptive.ui.checkbox.api.checkbox
import `fun`.adaptive.ui.checkbox.api.theme.checkboxTheme
import `fun`.adaptive.ui.datetime.datetime
import `fun`.adaptive.ui.dialog.api.dangerButtonDialog
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.label.smallLabel
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.utility.UUID
import kotlinx.coroutines.launch

@Adaptive
fun accountEditor(account: AccountEditorData? = null, close: () -> Unit) {

    @Independent
    var copy = copyStore { account ?: AccountEditorData() }

    @Independent
    val principal = fetch { getService<PrincipalApi>(adapter().transport).get(account?.id?.cast() ?: UUID.nil()) }

    val knownRoles = fetch { getService<RoleApi>(adapter().transport).all() } ?: emptyList()
    val principalRoles = fetch { getService<RoleApi>(adapter().transport).rolesOf(account?.id?.cast() ?: UUID.nil(), null) } ?: emptyList()

    val rowCount = if (account == null) 4 else 6

    grid {
        rowTemplate(72.dp repeat rowCount, 86.dp)
        width { 708.dp } .. height { (24 + 72 * rowCount + rowCount * 16 + 120 + 86).dp }
        paddingTop { 24.dp } .. gap { 16.dp }

        line { common(copy) }
        line { name(copy) }
        line { email(copy) }
        line { phone(copy) }

        if (principal != null) loginTimes(principal)
        if (principal != null) loginCounters(principal)

        roles(knownRoles, principalRoles)

        buttons(account, copy, close)
    }

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

        smallLabel { "Fiók név" } .. alignSelf.startCenter
        smallLabel { "Aktivált" }
        smallLabel { "Zárolt" }

        editor { account.login } .. width { 300.dp } .. alignSelf.startCenter
        checkbox { account.activated }
        checkbox { account.locked }
    }
}

@Adaptive
fun name(account: AccountEditorData) {
    grid {
        maxWidth
        rowTemplate(28.dp, 44.dp)
        alignItems.startCenter

        smallLabel { "Név" }
        editor { account.name } .. width { 600.dp }
    }
}

@Adaptive
fun email(account: AccountEditorData) {
    grid {
        rowTemplate(28.dp, 44.dp)
        alignItems.startCenter
        gapWidth { 32.dp }

        smallLabel { "E-mail cím" }
        editor { account.email } .. width { 600.dp }
    }
}

@Adaptive
fun phone(account: AccountEditorData) {
    grid {
        maxWidth
        rowTemplate(28.dp, 44.dp)
        alignItems.startCenter

        smallLabel { "Telefonszám" }
        editor { account.phone }
    }
}

@Adaptive
fun loginTimes(principal: Principal) {
    grid {
        maxWidth
        rowTemplate(28.dp, 44.dp)
        colTemplate(1.fr repeat 2)
        gapWidth { 32.dp }
        alignItems.startCenter

        smallLabel { "Utolsó sikeres azonosítás" }
        smallLabel { "Utolsó sikertelen azonosítás" }

        datetime(principal.lastAuthSuccess)
        datetime(principal.lastAuthFail)
    }
}

@Adaptive
fun loginCounters(principal: Principal) {
    grid {
        maxWidth
        rowTemplate(28.dp, 44.dp)
        colTemplate(1.fr repeat 2)
        gapWidth { 32.dp }
        alignItems.startCenter

        smallLabel { "Sikeres azonosítások száma" }
        smallLabel { "Sikertelen azonosítások száma" }

        text(principal.authSuccessCount)
        text(principal.authFailCount)
    }
}

@Adaptive
fun roles(knownRoles: List<Role>, principalRoles: List<Role>) {

    var selectedRoles = principalRoles

    column {
        paddingLeft { 32.dp } .. paddingRight { 16.dp } .. height { 120.dp } .. gap { 8.dp }

        smallLabel { "Szerepkörök" }

        flowBox {
            gap { 8.dp }

            for (role in knownRoles) {
                row {
                    alignItems.center .. gap { 8.dp }

                    onClick {
                        if (role in selectedRoles) {
                            selectedRoles -= role
                        } else {
                            selectedRoles += role
                        }
                    }

                    box {
                        size(24.dp, 24.dp) .. alignItems.center

                        if (role in selectedRoles) {
                            box(*checkboxTheme.active) {
                                svg(Res.drawable.check, *checkboxTheme.icon)
                            }
                        } else {
                            box(*checkboxTheme.inactive) {

                            }
                        }
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

            dangerButtonDialog("Reset", Res.drawable.gpp_maybe, "Network Reset") { closeInner ->
//                resetNetwork(copy) {
//                    closeInner()
//                }
            }

            dangerButton("Csatlakoztatás", Res.drawable.gpp_maybe) .. onClick {
//                io {
//                    modelService.addCommand(EnableJoin(copy.id))
//                    info("Csatlakozás engedélyezve.")
//                    close()
//                }
            }
        }

        button("Mentés", Res.drawable.check) .. onClick {
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