package `fun`.adaptive.lib.util.error

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.service.model.ReturnException

/**
 * An exception that indicates that the user tried an operation which failed a
 * requirement and there is an explanation for the reason.
 *
 * @property   [key]         key to a resource that explains the situation
 * @property   [arguments]   arguments of the exception, must be wireformat capable
 */
@Adat
class RequirementException(
    val key: String,
    val arguments: List<Any>
) : ReturnException()