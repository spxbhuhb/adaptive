package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.frontend.FolderFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.Path

/**
 * Registers an Auto list with [AutoWorker].
 *
 * After registration peers can use [autoList] to connect to the registered
 * list. To get the connection info needed for the [autoList] use the `connectInfo`
 * function of the returned frontend.
 *
 * - Adding/removing items from the list generate a new list instance (on all peers).
 * - Property changes (on any peer) generate a new list instance (on all peers).
 * - Property changes keep the non-affected instances.
 *
 * Each new instance is validated by default, so code that use values
 * produced by [autoList] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * The list is **NOT** thread safe.
 *
 * @param    worker             Origins that support peer connections must specify pass an [AutoWorker] in this
 *                              parameter. Standalone origins may pass `null`.
 *
 * @param    onListCommit       Called after the structure of the list has been changed (add/remove), but before the
 *                              state of the fragment is updated.
 *
 * @param    onItemCommit       Called when a property of a list item has been changed, but before the
 *                              state of the fragment is updated.
 *
 * @return   The Auto frontend of this list. Use this instance to change
 *           properties and to get connection info for the connecting peers.
 */
fun <A : AdatClass> autoFolderPoly(
    worker: AutoWorker?,
    path: Path,
    fileNameFun: (itemId: ItemId, item: A) -> String,
    wireFormatProvider: WireFormatProvider = Json,
    defaultWireFormat: AdatClassWireFormat<*>? = null,
    serviceContext: ServiceContext? = null,
    handle: AutoHandle = AutoHandle(),
    trace: Boolean = false,
    onListCommit: ((newValue: List<A>) -> Unit)? = null,
    onItemCommit: ((newValue: List<A>, item: A) -> Unit)? = null,
): OriginBase<SetBackend, FolderFrontend<A>, List<A>> {

    return OriginBase(
        worker,
        handle,
        serviceContext,
        defaultWireFormat,
        trace = trace
    ) {

        backend = SetBackend(
            context,
            FolderFrontend.load(context, path, wireFormatProvider)
        )

        frontend = FolderFrontend(
            backend,
            onListCommit,
            onItemCommit,
            wireFormatProvider,
            path,
            fileNameFun
        )

        frontend.commit()
    }

}