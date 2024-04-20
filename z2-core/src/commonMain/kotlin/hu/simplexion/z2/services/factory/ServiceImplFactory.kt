package hu.simplexion.z2.services.factory

import hu.simplexion.z2.services.ServiceContext
import hu.simplexion.z2.services.ServiceImpl

interface ServiceImplFactory {

    operator fun plusAssign(template: ServiceImpl<*>)

    operator fun get(serviceName: String, context: ServiceContext): ServiceImpl<*>?

}