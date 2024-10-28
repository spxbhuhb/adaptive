package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.adat.api.isValid
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.frontend.FileFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.internal.origin.OriginItemBase
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.utility.exists
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.Path

/**
 * Register a file-based origin Auto instance with the [worker].
 *
 * - backend: [PropertyBackend]
 * - frontend: [FileFrontend]
 *
 * Initialization:
 *
 * - load data from the file specified by [path], if it exists
 * - use [initialValue] if the file specified by [path] does not exist
 * - throw exception if the file does not exist and [initialValue] is null
 * - create the file if it does not exist
 *
 * [FileFrontend] writes all changes to the file specified by [path].
 *
 * After registration peers can use [autoItem] to connect to the registered
 * instance. To get the connection info needed for the [autoItem]
 * use the `connectInfo` function of the returned frontend.
 *
 * Property changes (on any peer) generate a new instance (on all peers).
 *
 * Each new instance is validated by default, so fragments that use values
 * produced by [autoItem] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * Registers a cleanup handler into the session through [serviceContext] or
 * into the context if there is no session.
 *
 * **This function and the instance is NOT thread safe.**
 *
 * @param    worker              Origins that support peer connections must specify pass an [AutoWorker] in this
 *                               parameter. Standalone origins may pass `null`.
 *
 * @param    wireFormatProvider  The wire format to use to read/write the content of the file.
 *
 * @param    itemId              The item id of the instance, default is (1,1). Passing an item id is useful when
 *                               the instance is part of an auto list, but we don't need the whole list, just
 *                               that one item.
 *
 * @return   An [OriginBase] for this auto instance. Use this instance to change
 *           properties and to get connection info for the connecting peers.
 *
 * @throws   IllegalArgumentException  if [initialValue] is `null` and no file exists on [path]
 */
fun <A : AdatClass> autoFile(
    worker: AutoWorker?,
    companion: AdatCompanion<A>,
    path: Path,
    initialValue: A? = null,
    wireFormatProvider: WireFormatProvider = Json,
    listener: AutoItemListener<A>? = null,
    serviceContext: ServiceContext? = null,
    handle: AutoHandle = AutoHandle(),
    itemId: ItemId = LamportTimestamp.CONNECTING,
    trace: Boolean = false,
): FileBase<A> {

    val pItemId : ItemId
    val propertyTimes : List<LamportTimestamp>
    val value: Array<Any?>
    val commit : Boolean

    val size = companion.adatMetadata.properties.size

    when {
        initialValue != null -> {
            pItemId = if (itemId === LamportTimestamp.CONNECTING) LamportTimestamp.ORIGIN else itemId
            value = initialValue.toArray()
            propertyTimes = MutableList(size) { pItemId }
            commit = true

            FileFrontend.write(path, wireFormatProvider, pItemId, propertyTimes, initialValue)
        }

        path.exists() -> {
            val r = FileFrontend.read<A>(path, wireFormatProvider)
            pItemId = r.first ?: itemId
            value = r.third.toArray()
            propertyTimes = r.second
            commit = true

            check(r.third.adatCompanion.wireFormatName == companion.wireFormatName) {
                "type mismatch in $path: ${r.third.adatCompanion.wireFormatName} != ${companion.wireFormatName}"
            }

            check(r.third.isValid()) { "validation failed for content of $path" }
        }

        else -> {
            pItemId = if (itemId === LamportTimestamp.CONNECTING) LamportTimestamp.ORIGIN else itemId
            value = arrayOfNulls<Any?>(size)
            propertyTimes = MutableList(size) { LamportTimestamp.CONNECTING }
            commit = false
        }
    }

    return OriginItemBase(
        worker,
        handle,
        serviceContext,
        companion.adatWireFormat,
        trace
    ) {

        if (listener != null) context.addListener(listener)

        backend = PropertyBackend(
            context,
            pItemId,
            null,
            value,
            propertyTimes
        )

        frontend = FileFrontend(
            backend,
            companion.adatWireFormat,
            pItemId,
            null, null,
            wireFormatProvider,
            path
        )

        if (commit) {
            frontend.commit(initial = true, fromBackend = false)
        }
    }
}