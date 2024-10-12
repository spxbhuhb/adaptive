package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.internal.frontend.CollectionFrontendBase
import `fun`.adaptive.auto.internal.frontend.FrontendBase
import `fun`.adaptive.auto.internal.frontend.InstanceFrontendBase
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.service.ServiceContext
import kotlin.time.Duration

class OriginInstanceBase<BE : BackendBase, FE : InstanceFrontendBase<IT>, IT : AdatClass>(
    worker: AutoWorker?,
    handle: AutoHandle,
    serviceContext: ServiceContext?,
    defaultWireFormat: AdatClassWireFormat<*>?,
    trace: Boolean,
    register: Boolean = true,
    builder: OriginBase<BE, FE, IT, IT>.() -> Unit
) : OriginBase<BE, FE, IT, IT> (
    worker, handle, serviceContext, defaultWireFormat, trace, register, builder
) {

    val value: IT
        inline get() = frontend.value

    override suspend fun connectDirect(
        waitForSync: Duration?,
        connectInfoFun: suspend () -> AutoConnectionInfo<IT>
    ): OriginInstanceBase<BE, FE, IT> {
        super.connectDirect(waitForSync, connectInfoFun)
        return this
    }

    fun update(newValue: IT) {
        frontend.update(newValue)
    }
}