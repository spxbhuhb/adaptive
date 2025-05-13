package `fun`.adaptive.supervisor.server

import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.auth.model.AuthMarkers
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.supervisor.api.SupervisorAppApi
import `fun`.adaptive.supervisor.model.AppSpec
import `fun`.adaptive.supervisor.model.SupervisorMarkers
import `fun`.adaptive.value.*
import `fun`.adaptive.value.util.serviceSubscribe

class SupervisorAppService : ServiceImpl<SupervisorAppService>(), SupervisorAppApi {

    val valueWorker by worker<AvValueWorker> { it.domain == "general" }

    override suspend fun subscribe(subscriptionId: AvSubscriptionId): List<AvSubscribeCondition> {
        ensureLoggedIn()
        return serviceSubscribe(valueWorker, subscriptionId, SupervisorMarkers.APPLICATION)
    }

    override suspend fun unsubscribe(id: AvSubscriptionId) {
        ensureLoggedIn()
        valueWorker.unsubscribe(id)
    }

    override suspend fun save(
        name: String,
        spec: AppSpec,
        parentId: AvValueId?,
        markers: Map<AvMarker, AvValueId?>?
    ) {
        ensureLoggedIn()

        val roleValue = AvValue(
            name = name,
            markersOrNull = setOf(AuthMarkers.ROLE),
            friendlyId = name,
            spec = spec
        )

        valueWorker.execute {
            val uniqueName = valueWorker.queryByMarker(AuthMarkers.ROLE).none { it.name == name }
            require(uniqueName) { "role with the same name already exists" }

            this += roleValue
        }
    }

    override suspend fun remove(id: AvValueId) {
        ensureLoggedIn()

        TODO("Not yet implemented")
    }

    override suspend fun start(id: AvValueId) {
        ensureLoggedIn()

        TODO("Not yet implemented")
    }

    override suspend fun stop(id: AvValueId) {
        ensureLoggedIn()

        TODO("Not yet implemented")
    }


}