package `fun`.adaptive.supervisor.server

import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.auth.model.AUTH_ROLE
import `fun`.adaptive.auth.model.AuthMarkers
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.supervisor.api.SupervisorAppApi
import `fun`.adaptive.supervisor.model.AppSpec
import `fun`.adaptive.supervisor.model.SupervisorMarkers
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueSubscriptionId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.util.serviceSubscribe

class SupervisorAppService : ServiceImpl<SupervisorAppService>(), SupervisorAppApi {

    val valueWorker by worker<AvValueWorker> { it.domain == "general" }

    override suspend fun subscribe(subscriptionId: AvValueSubscriptionId): List<AvSubscribeCondition> {
        ensureLoggedIn()
        return serviceSubscribe(valueWorker, subscriptionId, SupervisorMarkers.APPLICATION)
    }

    override suspend fun unsubscribe(id: AvValueSubscriptionId) {
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

        val roleValue = AvItem(
            name = name,
            type = AUTH_ROLE,
            parentId = null,
            markersOrNull = mutableMapOf(AuthMarkers.ROLE to null),
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