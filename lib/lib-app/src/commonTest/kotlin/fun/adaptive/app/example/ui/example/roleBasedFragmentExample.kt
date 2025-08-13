package `fun`.adaptive.app.example.ui.example

import `fun`.adaptive.app.ui.util.hasRole
import `fun`.adaptive.auth.model.AuthRoleId
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.runtime.hasRole
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.utility.UUID.Companion.uuid4

/**
 * # Role-based fragment example
 *
 * Use one of the [hasRole](function://) functions on `fragment()` to check if the user has a specific role.
 */
@Adaptive
fun roleBasedFragmentExample() {

    if (fragment().hasRole(name = exampleRoleName)) {
        text("The user has the role: $exampleRoleName")
    }

    if (fragment().hasRole(uuid = exampleRoleId)) {
        text("The user has the role: $exampleRoleId")
    }

}

// these would be decided by you or retrieved by other means
const val exampleRoleName = "role-name"
val exampleRoleId : AuthRoleId= uuid4()

