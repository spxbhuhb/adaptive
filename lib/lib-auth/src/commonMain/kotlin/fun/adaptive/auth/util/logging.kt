package `fun`.adaptive.auth.util

import `fun`.adaptive.auth.context.getPrincipalIdOrNull
import `fun`.adaptive.backend.builtin.ServiceImpl

fun ServiceImpl<*>.info(message : () -> String) {
    logger.info("@${serviceContext.getPrincipalIdOrNull()} ${message()}")
}