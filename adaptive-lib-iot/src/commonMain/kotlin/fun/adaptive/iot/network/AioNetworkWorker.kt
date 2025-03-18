package `fun`.adaptive.iot.network

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.iot.common.AioMarkerSet
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use

class AioNetworkWorker : WorkerImpl<AioNetworkWorker> {

    private val lock = getLock()

    private val networks = mutableMapOf<AioNetworkId, AioNetwork>()

    override suspend fun run() {

    }

    fun query(markerSet: AioMarkerSet): List<AioNetwork> =
        lock.use {
            if (markerSet.isEmpty()) {
                networks.values.toList()
            } else {
                networks.values.filter { it.markers.containsAll(markerSet) }
            }
        }

    fun add(network: AioNetwork) {
        lock.use {
            check(network.uuid !in networks) { "network with uuid ${network.uuid} already exists" }
            networks[network.uuid] = network
        }
    }

    fun update(network: AioNetwork) {
        lock.use {
            check(network.uuid in networks) { "network with uuid ${network.uuid} does not exist" }
            networks[network.uuid] = network
        }
    }

    fun remove(network: AioNetwork) {
        lock.use {
            networks.remove(network.uuid)
        }
    }

}