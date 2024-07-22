package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.lib.auth.worker.SessionWorker
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.utility.UUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.session(sessionWorker: SessionWorker) {

    get("/adaptive/session-id") {
        val existingSessionId = call.request.cookies[sessionWorker.sessionCookieName]?.let { UUID<ServiceContext>(it) }

        val id = if (existingSessionId != null && sessionWorker.getSessionForContext(existingSessionId) != null) {
            existingSessionId
        } else {
            UUID()
        }.toString()

        call.response.cookies.append(sessionWorker.sessionCookieName, id, httpOnly = true, path = "/")
        call.respond(HttpStatusCode.OK)
    }

}