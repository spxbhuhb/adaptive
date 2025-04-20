package `fun`.adaptive.app.ws.auth.admin.role

import `fun`.adaptive.lib_app.generated.resources.addRole
import `fun`.adaptive.lib_app.generated.resources.context
import `fun`.adaptive.lib_app.generated.resources.name
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.auth.model.AUTH_ROLE
import `fun`.adaptive.auth.model.RoleSpec
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.popup.modalForEdit
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.value.item.AvItem

@Adaptive
fun roleEditor(
    role: AvItem<RoleSpec>? = null,
    hide: () -> Unit,
    save: (AvItem<RoleSpec>) -> Unit
) {
    @Independent
    var copy = copyOf { role ?: AvItem("", AUTH_ROLE, friendlyId = "", spec = RoleSpec()) }

    modalForEdit(Strings.addRole, hide, { save(copy); hide() }) {
        row {
            editFields(copy)
        }
    }
}

@Adaptive
fun editFields(copy: AvItem<RoleSpec>) {

    column {
        width { 400.dp } .. padding { 16.dp } .. gap { 8.dp } .. borderRight(colors.lightOutline)

        withLabel(Strings.name) { state ->
            textInput(copy.name) { copy.update(copy::name, it) }
        }

        withLabel(Strings.context) { state ->
            textInput(copy.spec.context) { copy.update(copy::spec, copy.spec.copy(context = it)) }
        }

    }
}