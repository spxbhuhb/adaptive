package `fun`.adaptive.app.ws.auth.admin.role

import `fun`.adaptive.auth.model.AUTH_ROLE
import `fun`.adaptive.auth.model.RoleSpec
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.lib_app.generated.resources.addRole
import `fun`.adaptive.lib_app.generated.resources.editRole
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.popup.modalForEdit
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.value.item.AvItem

@Adaptive
fun roleEditor(
    role: AvItem<RoleSpec>? = null,
    hide: () -> Unit,
    save: (AvItem<RoleSpec>) -> Unit
) {

    val template = AvItem("", AUTH_ROLE, friendlyId = "", spec = RoleSpec())
    val title = if (role == null) Strings.addRole else Strings.editRole

    @Independent
    val form = adatFormBackend(role ?: template)

    modalForEdit(title, hide, { save(form.inputValue); hide() }) {
        column {
            width { 400.dp } .. padding { 16.dp } .. gap { 8.dp } .. borderRight(colors.lightOutline)

            localContext(form) {
                textEditor { template.name }
                textEditor { template.spec.context }
            }
        }
    }

}