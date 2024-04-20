package hu.simplexion.z2.services.transport

class ServiceTimeoutException(
    val serviceName: String,
    val funName: String,
    val responseEnvelope: ResponseEnvelope
) : RuntimeException()