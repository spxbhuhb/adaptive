package `fun`.adaptive.app.ui.common.admin.role

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
import `fun`.adaptive.ui.popup.modal.editorModal
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.value.AvValue

@Adaptive
fun roleEditorAdmin(
    roleAndBackend: Pair<AvValue<RoleSpec>?, RoleManagerViewBackend>,
    hide: UiClose
) {
    val role = roleAndBackend.first
    val backend = roleAndBackend.second

    val template = AvValue(spec = RoleSpec())
    val title = if (role == null) Strings.addRole else Strings.editRole

    @Independent
    val form = adatFormBackend(role ?: template)

    editorModal(title, hide, { backend.save(form.inputValue, role == null); hide.uiClose() }) {
        column {
            width { 400.dp } .. padding { 16.dp } .. gap { 8.dp } .. borderRight(colors.lightOutline)

            localContext(form) {
                textEditor { template.name }
                textEditor { template.spec.context }
            }
        }
    }
}