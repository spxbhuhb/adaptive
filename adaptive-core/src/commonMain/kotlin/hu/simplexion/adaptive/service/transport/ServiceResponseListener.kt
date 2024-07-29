package hu.simplexion.adaptive.service.transport

import hu.simplexion.adaptive.service.ServiceResponseEndpoint
import hu.simplexion.adaptive.service.model.ResponseEnvelope

interface ServiceResponseListener {
    suspend fun receive(endpoint: ServiceResponseEndpoint, message: ResponseEnvelope)
}