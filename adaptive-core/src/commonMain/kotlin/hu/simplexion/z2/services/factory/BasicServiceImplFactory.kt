package hu.simplexion.z2.services.factory

import hu.simplexion.z2.services.ServiceContext
import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.utility.Lock
import hu.simplexion.z2.utility.use

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