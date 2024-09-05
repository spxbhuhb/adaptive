package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.frontend.FolderFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.utility.exists
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.io.files.Path

/**
 * Register a directory-based Auto list with the [worker].
 *
 * - backend: [SetBackend]
 * - frontend: [FolderFrontend]
 *
 * Initialization:
 *
 * - throw exception if the directory specified by [path] does not exist
 * - load each file in the directory with `FileFrontend.read`
 * - add each loaded item to the list
 *
 * [FolderFrontend] writes all changes to the files specified by [path] and [fileNameFun].
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
 * produced by [originList] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * Registers a cleanup handler into the session through [serviceContext] or
 * into the context if there is no session.
 *
 * **This function, the list and the instances are NOT thread safe.**
 *
 * @param    onListCommit       Called after the structure of the list has been changed (add/remove), but before the
 *                              state of the fragment is updated.
 * @param    onItemCommit       Called when a property of a list item has been changed, but before the
 *                              state of the fragment is updated.
 *
 * @return   The Auto frontend of this list. Use this instance to change
 *           properties and to get connection info for the connecting peers.
 *
 * @throws   IllegalArgumentException  if the directory does not exist
 */
fun <A : AdatClass<A>> originFolder(
    worker: AutoWorker,
    companion: AdatCompanion<A>,
    wireFormatProvider: WireFormatProvider,
    path: Path,
    fileNameFun: (itemId: ItemId, item: A) -> String,
    serviceContext: ServiceContext? = null,
    trace: Boolean = false,
    onListCommit: ((newValue: List<A>) -> Unit)? = null,
    onItemCommit: ((newValue: List<A>, item: A) -> Unit)? = null,
): OriginBase<SetBackend, FolderFrontend<A>> {

    require(path.exists()) { "missing directory: $path" }

    return OriginBase(
        worker,
        serviceContext,
        companion.adatMetadata,
        companion.adatWireFormat,
        trace
    ) {

        backend = SetBackend(
            context,
            FolderFrontend.load(context, path, wireFormatProvider)
        )

        frontend = FolderFrontend(
            backend,
            companion,
            onListCommit,
            onItemCommit,
            wireFormatProvider,
            path,
            fileNameFun
        )
    }

}