package hu.simplexion.z2.services.transport

import hu.simplexion.z2.services.model.ResponseEnvelope

class ServiceResultException(
    val serviceName: String,
    val funName: String,
    val responseEnvelope: ResponseEnvelope
) : RuntimeException()