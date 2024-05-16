package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.utility.UUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.session() {
    get("/adaptive/session") {
        val existingSessionId = call.request.cookies["ADAPTIVE_SESSION"]?.let { UUID<ServiceContext>(it) }

        val id = if (existingSessionId != null /* && sessionImpl.getSessionForContext(existingSessionId) != null */) {
            existingSessionId
        } else {
            UUID()
        }.toString()

        call.response.cookies.append("ADAPTIVE_SESSION", id, httpOnly = true, path = "/")
        call.respond(HttpStatusCode.OK)
    }
}