package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.worker.AutoWorker
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatProvider

/**
 * Registers a copy of [initialValue] as an Auto list with [AutoWorker].
 *
 * After registration peers can use [autoList] to connect to the registered
 * list. To get the connection info needed for the [autoList] use the `connectInfo`
 * function of the returned frontend.
 *
 * - Adding/removing items from the list generate a new list instance (on all peers).
 * - Property changes (on any peer) generate a new list instance (on all peers).
 * - Property changes keep the non-affected instances.
 *
 * Each new instance is validated by default, so fragments that use values
 * produced by [autoList] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * @param    onListCommit       Called after the structure of the list has been changed (add/remove), but before the
 *                              state of the fragment is updated.
 * @param    onItemCommit       Called when a property of a list item has been changed, but before the
 *                            state of the fragment is updated.
 *
 * @return   The Auto frontend of this list. Use this instance to change
 *           properties and to get connection info for the connecting peers.
 */
fun <A : AdatClass<A>> originList(
    worker: AutoWorker,
    companion: AdatCompanion<A>,
    trace: Boolean = false,
    onListCommit: ((newValue: List<A>) -> Unit)? = null,
    onItemCommit: ((newValue: List<A>, item: A) -> Unit)? = null
) : AdatClassListFrontend<A> {

    val originHandle = AutoHandle(UUID(), 1)

    // TODO I'm not sure creating a logger for each instance is a good idea, think about this
    val logger = getLogger("fun.adaptive.auto.internal.producer.AutoList.${originHandle.globalId.toShort()}.${originHandle.clientId}")
    if (trace) logger.enableFine()

    val context = BackendContext(
        originHandle,
        worker.scope,
        logger,
        ProtoWireFormatProvider(),
        companion.adatMetadata,
        companion.adatWireFormat,
        LamportTimestamp(1, 1)
    )

    val originBackend = SetBackend(context)

    val originFrontend = AdatClassListFrontend(
        originBackend,
        companion,
        onListCommit = onListCommit,
        onItemCommit = onItemCommit
    )

    originBackend.frontEnd = originFrontend

    worker.register(originBackend)

    return originFrontend
}