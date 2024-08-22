package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.worker.AutoWorker
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatProvider

/**
 * Registers a copy of [initialValue] as an Auto instance with [AutoWorker].
 *
 * After registration peers can use [autoInstance] to connect to the registered
 * instance. To get the connection info needed for the [autoInstance]
 * use the `connectInfo` function of the returned frontend.
 *
 * Property changes (on any peer) generate a new instance (on all peers).
 *
 * Each new instance is validated by default, so fragments that use values
 * produced by [autoList] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * @param    onChange       Called after a new instance is generated (that is,
 *                          after a property changes).
 * @return   The Auto frontend of this instance. Use this instance to change
 *           properties and to get connection info for the connecting peers.
 */
fun <A : AdatClass<A>> originInstance(
    worker: AutoWorker,
    initialValue: A,
    trace: Boolean = false,
    onChange: ((newValue: A) -> Unit)? = null
) : AdatClassFrontend<A> {

    val originHandle = AutoHandle(UUID(), 1)

    // TODO I'm not sure creating a logger for each instance is a good idea, think about this
    val logger = getLogger("fun.adaptive.auto.internal.producer.AutoInstance.${originHandle.globalId.toShort()}.${originHandle.clientId}")
    if (trace) logger.enableFine()

    val companion = initialValue.adatCompanion

    val context = BackendContext(
        originHandle,
        worker.scope,
        logger,
        ProtoWireFormatProvider(),
        companion.adatMetadata,
        companion.adatWireFormat,
        LamportTimestamp(1, 1)
    )

    val originBackend = PropertyBackend(
        context,
        LamportTimestamp(1, 1),
        null,
        initialValue.toArray()
    )

    val originFrontend = AdatClassFrontend(
        originBackend,
        companion,
        initialValue,
        null, null
    ) {
        it.value?.let { value ->
            value.validate()
            onChange?.invoke(value)
        }
    }

    originBackend.frontEnd = originFrontend

    worker.register(originBackend)

    return originFrontend
}