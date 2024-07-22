package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.lib.auth.worker.SessionWorker
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.utility.UUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.clientId(sessionWorker: SessionWorker) {

    get(sessionWorker.clientIdRoute) {
        val existingClientId = call.request.cookies[sessionWorker.clientIdCookieName]?.let { UUID<ServiceContext>(it) }

        val id = if (existingClientId != null && sessionWorker.getSessionForContext(existingClientId) != null) {
            existingClientId
        } else {
            UUID()
        }.toString()

        call.response.cookies.append(sessionWorker.clientIdCookieName, id, httpOnly = true, path = "/")
        call.respond(HttpStatusCode.OK)
    }

}