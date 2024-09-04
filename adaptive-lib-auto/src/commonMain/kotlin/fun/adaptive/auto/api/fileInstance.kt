package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.frontend.FileFrontend
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.exists
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import kotlinx.io.files.Path

/**
 * Registers an Auto instance with a [FileFrontend] with [AutoWorker].
 *
 * The data is loaded from the file specified by [path]. The function
 * throws an exception if no such file exists **AND** [initialValue] is null.
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
 * The instance is **NOT** thread safe.
 *
 * @return   The Auto frontend of this instance. Use this instance to change
 *           properties and to get connection info for the connecting peers.
 */
fun <A : AdatClass<A>> fileInstance(
    worker: AutoWorker,
    companion: AdatCompanion<A>,
    path: Path,
    wireFormatProvider: WireFormatProvider,
    initialValue: A?,
    trace: Boolean = false
): FileFrontend<A> {

    val originHandle = AutoHandle(UUID(), 1)

    // TODO I'm not sure creating a logger for each instance is a good idea, think about this
    val logger = getLogger("fun.adaptive.auto.api.fileInstance.${originHandle.globalId.toShort()}.${originHandle.clientId}")
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

    val values = if (! path.exists()) {
        requireNotNull(initialValue) { "no initial value and the file $path does not exist" }
        wireFormatProvider.write(path, initialValue)
        initialValue.toArray()
    } else {
        wireFormatProvider.read(path, companion.adatWireFormat).toArray()
    }

    val originBackend = PropertyBackend(
        context,
        LamportTimestamp(1, 1),
        companion.wireFormatName,
        values
    )

    val originFrontend = FileFrontend(
        originBackend,
        companion.adatWireFormat,
        null, null, null,
        wireFormatProvider,
        path
    )

    originBackend.frontEnd = originFrontend

    worker.register(originBackend)

    return originFrontend
}