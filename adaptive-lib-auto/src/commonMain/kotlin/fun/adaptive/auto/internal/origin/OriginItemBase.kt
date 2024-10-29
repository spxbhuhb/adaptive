package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.frontend.AutoItemFrontend
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.service.ServiceContext
import kotlin.time.Duration

class OriginItemBase<BE : AutoBackend, FE : AutoItemFrontend<IT>, IT : AdatClass>(
    worker: AutoWorker?,
    handle: AutoHandle,
    serviceContext: ServiceContext?,
    defaultWireFormat: AdatClassWireFormat<*>?,
    trace: Boolean,
    register: Boolean = true,
    builder: AutoInstance<BE, FE, IT, IT>.() -> Unit,
) : AutoInstance<BE, FE, IT, IT>(
    worker, handle, serviceContext, defaultWireFormat, trace, register, builder
) {

    val value: IT
        inline get() = frontend.value

    override suspend fun connectDirect(
        waitForSync: Duration?,
        connectInfoFun: suspend () -> AutoConnectionInfo<IT>,
    ): OriginItemBase<BE, FE, IT> {
        super.connectDirect(waitForSync, connectInfoFun)
        return this
    }

}