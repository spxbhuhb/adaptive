package hu.simplexion.z2.services.transport

class ServiceResultException(
    val serviceName: String,
    val funName: String,
    val responseEnvelope: ResponseEnvelope
) : RuntimeException()