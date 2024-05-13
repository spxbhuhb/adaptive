/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.factory

import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.utility.Lock
import hu.simplexion.adaptive.utility.getLock
import hu.simplexion.adaptive.utility.use

class BasicServiceImplFactory : ServiceImplFactory {

    private val templates = mutableMapOf<String, ServiceImpl<*>>()

    private val lock = getLock()

    override fun plusAssign(template: ServiceImpl<*>) {
        lock.use {
            check(template.serviceName !in templates) { "duplicate registration of service ${template.serviceName}" }
            templates[template.serviceName] = template
        }
    }

    override fun minusAssign(template: ServiceImpl<*>) {
        lock.use {
            templates.remove(template.serviceName)
        }
    }

    override fun get(serviceName: String, context: ServiceContext): ServiceImpl<*>? =
        lock.use {
            templates[serviceName]?.newInstance(context)
        }

}