package hu.simplexion.z2.services.factory

import hu.simplexion.z2.services.ServiceContext
import hu.simplexion.z2.services.ServiceImpl

class BasicServiceImplFactory : ServiceImplFactory {

    val templates = mutableMapOf<String, ServiceImpl<*>>()

    override fun plusAssign(template: ServiceImpl<*>) {
        templates[template.serviceName] = template
    }

    override fun get(serviceName: String, context: ServiceContext): ServiceImpl<*>? =
        templates[serviceName]?.newInstance(context)

}