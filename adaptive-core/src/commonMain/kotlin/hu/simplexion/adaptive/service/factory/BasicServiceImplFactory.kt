/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.factory

import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.ServiceImpl
import hu.simplexion.adaptive.utility.Lock
import hu.simplexion.adaptive.utility.use

class BasicServiceImplFactory : ServiceImplFactory {

    private val templates = mutableMapOf<String, ServiceImpl<*>>()

    private val lock = Lock()

    override fun plusAssign(template: ServiceImpl<*>) {
        lock.use {
            check(template.fqName !in templates) { "duplicate registration of service ${template.fqName}" }
            templates[template.fqName] = template
        }
    }

    override fun minusAssign(template: ServiceImpl<*>) {
        lock.use {
            templates.remove(template.fqName)
        }
    }

    override fun get(serviceName: String, context: ServiceContext): ServiceImpl<*>? =
        lock.use {
            templates[serviceName]?.newInstance(context)
        }

}