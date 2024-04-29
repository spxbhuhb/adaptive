/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.model

enum class ServiceCallStatus(
    val value: Int
) {
    Ok(0),
    ServiceNotFound(1),
    FunctionNotFound(2),
    Exception(3),
    Timeout(4),
    AccessDenied(5),
    AuthenticationFail(6),
    AuthenticationFailLocked(7),
    InvalidSession(8),
    Logout(9)
}