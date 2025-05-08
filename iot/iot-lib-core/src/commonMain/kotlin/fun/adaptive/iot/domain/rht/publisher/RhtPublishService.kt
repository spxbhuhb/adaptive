package `fun`.adaptive.iot.domain.rht.publisher

import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.app.IoTValueDomain
import `fun`.adaptive.iot.haystack.PhScienceMarkers
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.value.*
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.local.AvMarkedValueId
import `fun`.adaptive.value.local.AvMarkedValueSubscriptionResult
import `fun`.adaptive.value.util.serviceSubscribe

class RhtPublishService : ServiceImpl<RhtPublishService>(), RhtPublishApi {

    companion object {
        lateinit var worker: AvValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)
        worker = safeAdapter.firstImpl<AvValueWorker> { it.domain == IoTValueDomain }
    }

    override suspend fun subscribe(
        subscriptionId: AvValueSubscriptionId,
        spaceIds: List<AvValueId>
    ): AvMarkedValueSubscriptionResult {
        ensureLoggedIn()

        val conditions = mutableListOf<AvSubscribeCondition>()
        val mapping = mutableMapOf<AvValueId, MutableList<AvMarkedValueId>>()

        spaceIds.forEach { spaceId ->
            worker.refValList(spaceId, PointMarkers.POINTS).forEach {
                rhtMapPoint(spaceId, it, conditions, mapping)
            }
        }

        serviceSubscribe(worker, subscriptionId, conditions)

        return AvMarkedValueSubscriptionResult(conditions, mapping)
    }

    private fun rhtMapPoint(
        spaceId : AvValueId,
        point: AvValue,
        conditions: MutableList<AvSubscribeCondition>,
        mapping: MutableMap<AvValueId, MutableList<AvMarkedValueId>>
    ) {
        if (point !is AvItem<*>) return
        val markers = point.markersOrNull ?: return
        val curValRef = markers[PointMarkers.CUR_VAL] ?: return

        val marker : AvMarker

        when {
            PhScienceMarkers.TEMP in markers -> marker = PhScienceMarkers.TEMP
            PhScienceMarkers.HUMIDITY in markers -> marker = PhScienceMarkers.HUMIDITY
            else -> return
        }

        mapping.getOrPut(spaceId) { mutableListOf() } += AvMarkedValueId(marker, curValRef)
        conditions += AvSubscribeCondition(curValRef)
    }

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
        ensureLoggedIn()

        worker.unsubscribe(subscriptionId)
    }

}