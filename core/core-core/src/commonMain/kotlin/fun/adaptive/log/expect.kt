package `fun`.adaptive.log

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Reports a warning with [lazyMessage] to [logger] if [value] is null
 *
 * Useful to keep the application running in case of a minor inconsistency
 * while letting the developer know that there is a problem.
 *
 * @return  [value]
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> expectNotNull(
    logger : AdaptiveLogger,
    value : T?,
    lazyMessage : () -> String
) : T? {
    contract {
        returns() implies (value != null)
    }

    if (value == null) {
        logger.warning(lazyMessage())
        return null
    } else {
        return value
    }
}
