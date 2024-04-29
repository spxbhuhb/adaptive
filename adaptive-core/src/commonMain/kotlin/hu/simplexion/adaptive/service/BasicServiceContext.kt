/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.utility.UUID

data class BasicServiceContext(
    override val uuid: UUID<ServiceContext> = UUID(),
    override var data: MutableMap<Any, Any?> = mutableMapOf()
) : ServiceContext