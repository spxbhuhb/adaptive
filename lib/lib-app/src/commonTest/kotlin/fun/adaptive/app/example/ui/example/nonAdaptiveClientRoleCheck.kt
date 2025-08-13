package `fun`.adaptive.app.example.ui.example

import `fun`.adaptive.auth.model.AuthRoleId
import `fun`.adaptive.runtime.AbstractClientApplication
import `fun`.adaptive.utility.UUID.Companion.uuid4

/**
 * # Check for a role in non-adaptive client code
 *
 * Use one of the [hasRole](function://) functions on the application to
 * check if the user has a specific role.
 */
fun nonAdaptiveClientRoleCheck(
    application : AbstractClientApplication<*,*>
) {
    if (application.hasRole(roleName = exampleRoleName2)) {
        println("The user has the role: $exampleRoleName2")
    }

    if (application.hasRole(roleUuid = exampleRoleId2)) {
        println("The user has the role: $exampleRoleId2")
    }
}

// these would be decided by you or retrieved by other means
const val exampleRoleName2 = "role-name"
val exampleRoleId2 : AuthRoleId= uuid4()

