/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.services

import hu.simplexion.adaptive.utility.UUID

interface ServiceContext {
    val uuid: UUID<ServiceContext>
    var data: MutableMap<Any, Any?>
}