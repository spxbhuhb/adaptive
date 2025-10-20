package `fun`.adaptive.auth.example

import `fun`.adaptive.service.auth.ensureAll
import `fun`.adaptive.service.auth.ensureFailNotImplemented
import `fun`.adaptive.service.auth.ensureHas
import `fun`.adaptive.service.auth.ensureHasOrInternal
import `fun`.adaptive.service.auth.ensureInternal
import `fun`.adaptive.service.auth.ensureLoggedIn
import `fun`.adaptive.service.auth.ensureOneOf
import `fun`.adaptive.service.auth.ensurePrincipal
import `fun`.adaptive.service.auth.ensurePrincipalOrHas
import `fun`.adaptive.service.auth.ensurePrincipalOrOneOf
import `fun`.adaptive.service.auth.ensureTrue
import `fun`.adaptive.service.auth.ensuredBy
import `fun`.adaptive.service.auth.ensuredByLogic
import `fun`.adaptive.service.auth.publicAccess
import `fun`.adaptive.auth.model.AuthPrincipalId
import `fun`.adaptive.auth.model.AuthRoleId
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.utility.UUID.Companion.uuid4

/**
 * # Authorize from service implementations
 *
 * Call one (or more) of the `ensure` functions.
 *
 * All `ensure` functions throw [AccessDenied](class://) if the authorization fails, except
 * [ensureFailNotImplemented](function://) which throws `NotImplementedError`.
 */
class ServiceEnsureExample : ServiceImpl<ServiceEnsureExample>() {

    /**
     * Check for an arbitrary condition.
     */
    fun ensureTrueExample() {
        ensureTrue(true)
    }

    /**
     * Check for internal access (from other parts of the application).
     */
    fun ensureInternalExample() {
        ensureInternal()
    }

    /**
     * Mark a block as ensured by custom logic returning a boolean value
     * (a simple true value in this example).
     */
    fun ensuredByExample() {
        ensuredBy { true }
    }

    /**
     * Mark the following code as publicly accessible.
     */
    fun publicAccessExample() {
        publicAccess()
    }

    /**
     * Mark the following code as secured by some logic explanation.
     */
    fun ensuredByLogicExample() {
        ensuredByLogic("secured by external policy")
    }

    /**
     * Check that the caller is logged in.
     */
    fun ensureLoggedInExample() {
        ensureLoggedIn()
    }

    // these would be initialized with a proper role ids
    val requiredRoleId1: AuthRoleId = uuid4()
    val requiredRoleId2: AuthRoleId = uuid4()

    /**
     * Check for a specific role.
     */
    fun ensureHasExample() {
        ensureHas(requiredRoleId1)
    }

    /**
     * Check for a specific role or internal access.
     */
    fun ensureHasOrInternalExample() {
        ensureHasOrInternal(requiredRoleId1)
    }

    /**
     * Check that the caller has ALL required roles (by role ids).
     */
    fun ensureAllRoleIdsExample() {
        ensureAll(requiredRoleId1, requiredRoleId2)
    }

    /**
     * Check that caller has AT LEAST ONE of the given roles (by role ids).
     */
    fun ensureOneOfRoleIdsExample() {
        ensureOneOf(requiredRoleId1, requiredRoleId2)
    }

    // this would be initialized with a proper principal id
    val requiredPrincipalId: AuthPrincipalId = uuid4()

    /**
     * Check that the service runs in the name of the required principal.
     */
    fun ensurePrincipalExample() {
        ensurePrincipal(requiredPrincipalId)
    }

    /**
     * Check that the service runs in the name of the principal OR has the role.
     */
    fun ensurePrincipalOrHasExample() {
        ensurePrincipalOrHas(requiredPrincipalId, requiredRoleId1)
    }

    /**
     * Check that the service runs in the name of the principal OR has one of the roles.
     */
    fun ensurePrincipalOrOneOfExample() {
        ensurePrincipalOrOneOf(requiredPrincipalId, arrayOf(requiredRoleId1, requiredRoleId2))
    }

    /**
     * Example of intentional failure for not implemented code.
     */
    fun ensureFailNotImplementedExample() {
        ensureFailNotImplemented { "not implemented" }
    }
}