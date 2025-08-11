package `fun`.adaptive.app.example.ui.example

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.app.ui.util.hasRole
import `fun`.adaptive.auth.model.AuthRoleId
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.utility.UUID.Companion.uuid4

/**
 * # Check for a role in non-adaptive client code
 *
 * Use one of the [hasRole](function://) functions on the application to
 * check if the user has a specific role.
 */
fun nonAdaptiveClientRoleCheck(
    application : ClientApplication<*,*>
) {

    if (application.hasRole(name = exampleRoleName2)) {
        println("The user has the role: $exampleRoleName2")
    }

    if (fragment().hasRole(uuid = exampleRoleId2)) {
        println("The user has the role: $exampleRoleId2")
    }

}

// these would be decided by you or retrieved by other means
const val exampleRoleName2 = "role-name"
val exampleRoleId2 : AuthRoleId= uuid4()

