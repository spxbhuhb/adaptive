package `fun`.adaptive.auto.internal.connector

import `fun`.adaptive.auto.api.AutoApi
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.service.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.safeSuspendCall
import `fun`.adaptive.utility.untilNoTimeout
import `fun`.adaptive.utility.untilNotNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend fun <VT> serviceConnect(
    backend: BackendBase,
    transport: ServiceCallTransport,
    connectInfoFun: suspend () -> AutoConnectionInfo<VT>,
): AutoConnectionInfo<VT> {

    val autoService = getService<AutoApi>(transport)

    val connectInfo = untilNotNull { connectInfoFun() }

    untilNoTimeout {
        autoService.addPeer(
            connectInfo.originHandle,
            connectInfo.connectingHandle,
            backend.context.time
        )
    }

    val connector = ServiceConnector(
        backend,
        connectInfo.originHandle,
        autoService,
        reconnect = true
    )

    backend.addPeer(
        connector,
        connectInfo.originTime
    )

    // TODO in what scope should we start the service connection
    CoroutineScope(Dispatchers.Default).launch {
         connector.run()
    }

    return connectInfo

}