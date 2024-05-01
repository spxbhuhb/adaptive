/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.factory

import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.ServiceImpl

interface ServiceImplFactory {

    operator fun plusAssign(template: ServiceImpl<*,*>)

    operator fun minusAssign(template: ServiceImpl<*,*>)

    operator fun get(serviceName: String, context: ServiceContext): ServiceImpl<*,*>?

}